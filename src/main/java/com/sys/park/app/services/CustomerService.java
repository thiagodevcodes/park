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
import com.sys.park.app.dtos.Customer.CustomerForm;
import com.sys.park.app.dtos.Customer.CustomerFormUpdate;
import com.sys.park.app.dtos.Customer.CustomerGetDto;
import com.sys.park.app.dtos.CustomerType.CustomerTypeDto;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.models.CustomerModel;
import com.sys.park.app.repositories.CustomerRepository;
import com.sys.park.app.repositories.PersonRepository;
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
    private PersonRepository personRepository;

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
            throw new NotFoundException(
                    "Objeto não encontrado! Id Person: " + idPerson + ", Tipo: " + CustomerDto.class.getName());
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
            } else {
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível atualizar o cliente!");
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

    public void deleteById(Integer id) {
        try {
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);
            } else {
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Cliente!");
        }
    }

    public CustomerDto createNewCustomer(CustomerForm customerForm) {
        try { 
            PersonDto personDto = personService.getPersonByCustomer(customerForm);
            CustomerDto customerDto = new CustomerDto();
         
            if(personRepository.existsByCpf(customerForm.getCpf()) && !personDto.getCpf().equals(customerForm.getCpf()))
                throw new DataIntegrityException("CPF já cadastrado!");
            
            if(personRepository.existsByEmail(customerForm.getEmail()) && !personDto.getEmail().equals(customerForm.getEmail())) 
                throw new DataIntegrityException("Email já cadastrado!");
            
            customerDto.setIdCustomerType(customerForm.getClientType());
            customerDto.setPaymentDay(customerForm.getPaymentDay());
            customerDto.setIdPerson(personDto.getId());

            if (customerRepository.existsByIdPerson(personDto.getId())) {
                customerDto = this.findByIdPerson(personDto.getId());
                
                if(customerDto.getIsActive().equals(false)) { 
                    customerDto.setIsActive(true);
                    customerDto = this.updateById(customerDto, customerDto.getId());  
                } else {
                    throw new DataIntegrityException("Cliente já está ativo!");
                }
            } else {
                customerDto.setIsActive(true);
                customerDto = this.insert(customerDto);
            }
            
            return customerDto;
        } catch (

        DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível criar o cliente!", e);
        }
    }

    public CustomerDto updateCustomer(CustomerFormUpdate customerFormUpdate, Integer id) {
        try {
            CustomerDto customerDto = this.findById(id);
            PersonDto personDto = personService.findById(customerDto.getIdPerson());
           
            if(personRepository.existsByCpf(customerFormUpdate.getCpf()) && !personDto.getCpf().equals(customerFormUpdate.getCpf())) {
                throw new DataIntegrityException("CPF já cadastrado");
            }

            if(personRepository.existsByEmail(customerFormUpdate.getEmail()) && !personDto.getEmail().equals(customerFormUpdate.getEmail())) {
                throw new DataIntegrityException("Email já cadastrado");
            }

            personDto.setEmail(customerFormUpdate.getEmail());
            personDto.setPhone(customerFormUpdate.getPhone()); 
            personDto.setCpf(customerFormUpdate.getCpf());
            personDto.setName(customerFormUpdate.getName());
            personDto = personService.updateById(personDto, personDto.getId());
            
            customerDto.setIdPerson(personDto.getId());
            customerDto.setIdCustomerType(customerFormUpdate.getClientType());
            customerDto.setPaymentDay(customerFormUpdate.getPaymentDay());

            customerDto = this.updateById(customerDto, id);

            return customerDto;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não foi possível atualizar o cliente", e);
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

                if (personDto != null && customerTypeDto != null && customer.getIsActive()) {
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

            if (!customerExist.isPresent())
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");

            CustomerModel customerUpdated = customerExist.get();

            customerUpdated.setIsActive(false);
            System.out.println(customerUpdated);
            customerUpdated = customerRepository.save(customerUpdated);

            return modelMapper.map(customerUpdated, CustomerDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível finalizar o cliente!", e);
        }
    }
}
