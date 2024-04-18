package com.sys.park.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.Customer.CustomerForm;
import com.sys.park.app.dtos.Customer.CustomerMensalDto;
import com.sys.park.app.dtos.CustomerType.CustomerTypeDto;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.models.CustomerModel;
import com.sys.park.app.repositories.CustomerRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired 
    PersonService personService;

    @Autowired
    CustomerTypeService customerTypeService;
       
    @Autowired
    private ModelMapper modelMapper;

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

    public Page<CustomerMensalDto> findByCustomerTypePage(Integer customerType, Optional<Pageable> optionalPage) {
        try {
            Pageable page = optionalPage.orElse(Pageable.unpaged());
            Page<CustomerModel> customers = customerRepository.findByIdCustomerType(customerType, page);
            List<CustomerMensalDto> newDtoList = new ArrayList<>();
    
            for (CustomerModel customer : customers) {
                CustomerMensalDto newDto = new CustomerMensalDto();
    
                PersonDto personDto = personService.findById(customer.getIdPerson());
                CustomerTypeDto customerTypeDto = customerTypeService.findById(customer.getIdCustomerType());
    
                if(personDto != null && customerTypeDto != null && customer.getIsActive()) {
                    newDto.setId(customer.getId());
                    newDto.setCpf(personDto.getCpf());
                    newDto.setEmail(personDto.getEmail());
                    newDto.setName(personDto.getName());
                    newDto.setClientType(customerType);
                    newDto.setPhone(personDto.getPhone());
                    newDto.setPaymentDay(customer.getPaymentDay());
                    newDto.setIsActive(true);
                    newDtoList.add(newDto);
                }
            }

            Page<CustomerMensalDto> pageableClient = new PageImpl<>(newDtoList, page, customers.getTotalElements());
            return pageableClient; 
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível buscar os Clientes!");
        }
    }

    public List<CustomerDto> findAll() {
        try {
            List<CustomerModel> customerModelList = customerRepository.findAll();

            return customerModelList.stream()
                    .map(customer -> modelMapper.map(customer, CustomerDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar o Cliente!", e.getErrorMessages());
        }
    }

    public CustomerDto insert(CustomerDto customerDto) {
        try {
            CustomerModel newCustomer = modelMapper.map(customerDto, CustomerModel.class);
            
            newCustomer = customerRepository.save(newCustomer);
            return modelMapper.map(newCustomer, CustomerDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).");
        }
    }

    public CustomerDto updateById(CustomerForm customerForm, Integer id) {
        try {
            Optional<CustomerModel> customerExist = customerRepository.findById(id);

            if (customerExist.isPresent()) {
                CustomerModel customerUpdated = customerExist.get();
                
                modelMapper.map(customerForm, customerUpdated);
                customerUpdated.setId(id);
                customerUpdated = customerRepository.save(customerUpdated);

                return modelMapper.map(customerUpdated, CustomerDto.class);
            }else{
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);

            }else {
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Cliente!");
        }
    }

    public CustomerMensalDto createNewCustomer(CustomerForm customerForm, Integer customerType) {
        try {
            CustomerMensalDto newDto = new CustomerMensalDto();
            PersonDto personDto = new PersonDto();
            CustomerDto customerDto = new CustomerDto();

            if(customerType == 2) {
                personDto.setCpf(customerForm.getCpf());
                personDto.setEmail(customerForm.getEmail());
                personDto.setName(customerForm.getName());
                personDto.setPhone(customerForm.getPhone());

                if(personService.verifyCpfAndEmail(personDto.getCpf(), personDto.getEmail())) {
                    personDto = personService.findByCpf(personDto.getCpf());
                    customerDto = this.findByIdPerson(personDto.getId());

                    if(customerDto.getIsActive() == false) {
                        customerDto.setIsActive(true);
                        customerDto = this.updateById(customerForm, customerDto.getId());
                    } else {
                        throw new DataIntegrityException("O cliente já existe!");
                    }
                } else {
                    personDto = personService.insert(personDto, customerType);
                    customerDto.setIdPerson(personDto.getId());
                    customerDto.setIdCustomerType(customerType);
                    customerDto.setPaymentDay(customerForm.getPaymentDay());
                    customerDto.setIsActive(true);

                    customerDto = this.insert(customerDto);
                }
            } else if(customerType == 1) {
                personDto.setName(customerForm.getName());
                personDto = personService.insert(personDto, customerType);
                
                customerDto.setIdPerson(personDto.getId());
                customerDto.setIdCustomerType(customerType);
                customerDto.setPaymentDay(customerForm.getPaymentDay());
                customerDto.setIsActive(true);

                customerDto = this.insert(customerDto);
            }

            newDto.setCpf(personDto.getCpf());
            newDto.setEmail(personDto.getEmail());
            newDto.setName(personDto.getName());
            newDto.setPaymentDay(customerDto.getPaymentDay());
            newDto.setPhone(personDto.getPhone());
            newDto.setClientType(customerType);
        
            return newDto;

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).", e);
        }
    }

    public CustomerDto finishCustomer(Integer id) {
        try {
            Optional<CustomerModel> customerExist = customerRepository.findById(id);

            if (customerExist.isPresent()) {
                CustomerModel customerUpdated = customerExist.get();

                customerUpdated.setIsActive(false);
                customerUpdated = customerRepository.save(customerUpdated);

                return modelMapper.map(customerUpdated, CustomerDto.class);
            }else {
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).", e);
        }
    }


}
