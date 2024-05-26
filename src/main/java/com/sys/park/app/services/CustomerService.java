package com.sys.park.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.Customer.CustomerGetDto;
import com.sys.park.app.dtos.Customer.CustomerMensalDto;
import com.sys.park.app.dtos.CustomerType.CustomerTypeDto;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.models.CustomerModel;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.CustomerRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired 
    private PersonService personService;

    @Autowired
    private CustomerTypeService customerTypeService;
       
    @Autowired
    private ModelMapper modelMapper;

    public List<CustomerDto> findAll() {
        try {
            List<CustomerModel> customerModelList = customerRepository.findAll();

            return customerModelList.stream()
                    .map(customer -> modelMapper.map(customer, CustomerDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar os Clientes!");
        }
    }

    public CustomerDto findById(Integer id) {
        try {
            CustomerModel customerModel = customerRepository.findById(id).get();
            return modelMapper.map(customerModel, CustomerDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + CustomerDto.class.getName());
        }
    }

    public CustomerDto findByIdPerson(Integer idPerson) {
        try {
            CustomerModel customerModel = customerRepository.findByIdPerson(idPerson).get();
            return modelMapper.map(customerModel, CustomerDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id Person: " + idPerson + ", Tipo: " + CustomerDto.class.getName());
        }
    }

    public CustomerDto insert(CustomerDto customerDto) {
        try {
            CustomerModel newCustomer = modelMapper.map(customerDto, CustomerModel.class);
            
            newCustomer = customerRepository.save(newCustomer);
            return modelMapper.map(newCustomer, CustomerDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro ao tentar inserir um cliente!");
        }
    }

    public Boolean customerByIdPerson(Integer idPerson) {
        try {
            Optional<CustomerModel> userModel = customerRepository.findByIdPerson(idPerson);

            if (userModel.isPresent()) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException(
                    "Objeto não encontrado! Id Pessoa: " + idPerson + ", Tipo: " + UserModel.class.getName());
        }
    }

    public Boolean validCustomer(CustomerMensalDto user) {

        if (personService.validCpfAndEmail(user.getCpf(), user.getEmail())) {
            PersonDto newPerson = personService.findByCpf(user.getCpf());
            CustomerDto customerDto = new CustomerDto();

            if (this.customerByIdPerson(newPerson.getId())) {
                customerDto = this.findByIdPerson(newPerson.getId());

                if (!customerDto.getIsActive()) {
                    customerDto.setIsActive(true);
                    customerDto = this.updateById(customerDto, customerDto.getId());
                } else {
                    throw new BusinessRuleException("O cliente já existe!");
                }
            }
        }

        return true;
    }

    public CustomerMensalDto createNewCustomer(CustomerMensalDto customerMensalDto, Integer customerType) {
        try {
            this.validCustomer(customerMensalDto);
            CustomerMensalDto newCustomer = new CustomerMensalDto();
            PersonDto personDto = new PersonDto();
            CustomerDto customerDto = new CustomerDto();

            if(customerType == 2) {
                if(personService.verifyCpf(customerMensalDto.getCpf()) && personService.verifyEmail(customerMensalDto.getEmail())) {
                    personDto = personService.findByCpf(customerMensalDto.getCpf());
                    
                    if(!this.customerByIdPerson(personDto.getId())) {
                        customerDto.setIdPerson(personDto.getId());
                    }

                } else {
                    personDto.setCpf(customerMensalDto.getCpf());
                    personDto.setEmail(customerMensalDto.getEmail());
                    personDto.setName(customerMensalDto.getName());
                    personDto.setPhone(customerMensalDto.getPhone());

                    personDto = personService.insert(personDto);
                    customerDto.setIdPerson(personDto.getId());
                    
                }   
            } else if(customerType == 1) {
                personDto.setName(customerMensalDto.getName());
                personDto = personService.insert(personDto);
                
            }

            customerDto.setIdPerson(personDto.getId());
            customerDto.setIdCustomerType(customerType);
            customerDto.setPaymentDay(customerMensalDto.getPaymentDay());
            customerDto.setIsActive(true);

            customerDto = this.insert(customerDto);

            newCustomer.setId(customerDto.getId());
            newCustomer.setCpf(personDto.getCpf());
            newCustomer.setEmail(personDto.getEmail());
            newCustomer.setName(personDto.getName());
            newCustomer.setPaymentDay(customerDto.getPaymentDay());
            newCustomer.setPhone(personDto.getPhone());
            newCustomer.setClientType(customerType);
        
            return newCustomer;

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível criar o cliente!", e);
        }
    }

    public CustomerDto updateById(CustomerDto customerDto, Integer id) {
        try {
            Optional<CustomerModel> customerExist = customerRepository.findById(id);

            if (customerExist.isPresent()) {
                CustomerModel customerUpdated = customerExist.get();
                
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(customerDto, customerUpdated);

                customerUpdated = customerRepository.save(customerUpdated);

                return modelMapper.map(customerUpdated, CustomerDto.class);
            }else{
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível atualizar o cliente!");
        }
    }

    public CustomerMensalDto updateCustomer(CustomerMensalDto customerMensalDto, Integer id) {
        try {
            CustomerDto customerDto = this.findById(id);
            PersonDto personDto = personService.findById(customerDto.getIdPerson());
            
            if(!customerMensalDto.getCpf().equals(personDto.getCpf()) && !customerMensalDto.getEmail().equals(personDto.getEmail())) {
                personService.validCpfAndEmail(customerMensalDto.getCpf(), customerMensalDto.getEmail());
            }
        
            personDto.setName(customerMensalDto.getName());
            personDto.setCpf(customerMensalDto.getCpf());
            personDto.setEmail(customerMensalDto.getEmail());
            personDto.setPhone(customerMensalDto.getPhone());
            customerDto.setIdCustomerType(customerMensalDto.getClientType());
            customerDto.setPaymentDay(customerMensalDto.getPaymentDay());

            personDto = personService.updateById(personDto, personDto.getId());
            System.out.println(customerDto);
            customerDto = this.updateById(customerDto, id);

            CustomerMensalDto updatedCustomer = new CustomerMensalDto();

            
            updatedCustomer.setClientType(customerDto.getIdCustomerType());
            updatedCustomer.setCpf(personDto.getCpf());
            updatedCustomer.setEmail(personDto.getEmail());
            updatedCustomer.setName(personDto.getName());
            updatedCustomer.setPaymentDay(customerDto.getPaymentDay());
            updatedCustomer.setIsActive(customerDto.getIsActive());
            updatedCustomer.setPhone(personDto.getPhone());

            return updatedCustomer;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não foi possível atualizar o cliente", e);
        }
    }

    public void deleteById(Integer id) {
        try {
            Optional<CustomerModel> customerModel = customerRepository.findById(id);
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);
                personService.deleteById(customerModel.get().getIdPerson());
            }else {
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Cliente!");
        }
    }

    public Page<CustomerGetDto> getCustomersByCustomerType(Integer customerType, Optional<Pageable> optionalPage) {
        try {
            Pageable page = optionalPage.orElse(Pageable.unpaged());
            Page<CustomerModel> customers = customerRepository.findByIdCustomerTypeAndIsActive(customerType, true, page);
            List<CustomerGetDto> newDtoList = new ArrayList<>();
    
            for (CustomerModel customer : customers) {
                CustomerGetDto newDto = new CustomerGetDto();
    
                PersonDto personDto = personService.findById(customer.getIdPerson());
                CustomerTypeDto customerTypeDto = customerTypeService.findById(customer.getIdCustomerType());
    
                if(personDto != null && customerTypeDto != null && customer.getIsActive()) {
                    newDto.setId(customer.getId());
                    newDto.setCpf(personDto.getCpf());
                    newDto.setEmail(personDto.getEmail());
                    newDto.setName(personDto.getName());
                    newDto.setPhone(personDto.getPhone());
                    newDto.setPaymentDay(customer.getPaymentDay());
                    newDtoList.add(newDto);
                }
            }

            Page<CustomerGetDto> pageableClient = new PageImpl<>(newDtoList, page, customers.getTotalElements());
            return pageableClient; 
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível buscar os Clientes!");
        }
    }

    public CustomerDto finishCustomer(Integer id) {
        try {
            Optional<CustomerModel> customerExist = customerRepository.findById(id);

            if(!customerExist.isPresent()) 
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");

            CustomerModel customerUpdated = customerExist.get();

            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

            customerUpdated.setIsActive(false);
            customerUpdated = customerRepository.save(customerUpdated);

            return modelMapper.map(customerUpdated, CustomerDto.class);  
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível finalizar o cliente!", e);
        }
    }
}
