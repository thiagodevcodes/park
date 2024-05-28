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
import com.sys.park.app.dtos.Customer.CustomerGetDto;
import com.sys.park.app.dtos.Customer.CustomerMensalDto;
import com.sys.park.app.dtos.CustomerType.CustomerTypeDto;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.models.CustomerModel;
import com.sys.park.app.models.UserModel;
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
            System.out.println(customerModel);
            return modelMapper.map(customerModel, CustomerDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException(
                    "Objeto não encontrado! Id Person: " + idPerson + ", Tipo: " + CustomerDto.class.getName());
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

    public Boolean validCustomer(CustomerForm user) {
        PersonDto newPerson = new PersonDto();
        CustomerDto customerDto = new CustomerDto();
        Boolean isPresent = false;

        if (personRepository.existsByCpf(user.getCpf())) {
            newPerson = personService.findByCpf(user.getCpf());
            isPresent = true;
        } else if (personRepository.existsByEmail(user.getEmail())) {
            newPerson = personService.findByEmail(user.getEmail());
            isPresent = true;
        } else if(personRepository.existsByPhone(user.getPhone())) {
            newPerson = personService.findByPhone(user.getPhone());
            isPresent = true;
        }

        if (isPresent) {
            if (this.customerByIdPerson(newPerson.getId())) {
                customerDto = this.findByIdPerson(newPerson.getId());
                
                if (customerDto.getIsActive()) {
                               
                    if (personRepository.existsByPhone(user.getPhone()) && customerDto.getIsActive()) 
                        throw new BusinessRuleException("O celular já existe!");
                    
                    if (personRepository.existsByEmail(user.getEmail()) && customerDto.getIsActive())
                        throw new BusinessRuleException("O email já existe!");

                    if (personRepository.existsByCpf(user.getCpf()) && customerDto.getIsActive())
                        throw new BusinessRuleException("O CPF já existe!");   
                }
            }
        }

        return isPresent;
    }

    public CustomerDto createNewCustomer(CustomerForm customerForm, Integer customerType) {
        try {
            this.validCustomer(customerForm);
            PersonDto personDto = new PersonDto();
            CustomerDto customerDto = new CustomerDto();
            Boolean isPresent = false;

            if (customerType == 2) {
                if (personRepository.existsByEmail(customerForm.getEmail())) {
                    personDto = personService.findByEmail(customerForm.getEmail());   
                    CustomerDto oldCustomer = findById(1);
                    System.out.println(oldCustomer);
                    isPresent = true;    
                } else if (personRepository.existsByCpf(customerForm.getCpf())) {
                    personDto = personService.findByCpf(customerForm.getCpf());
                    isPresent = true; 
                } else if (personRepository.existsByPhone(customerForm.getPhone())) {
                    personDto = personService.findByPhone((customerForm.getPhone()));
                    isPresent = true; 
                } else {
                    personDto.setCpf(customerForm.getCpf());
                    personDto.setEmail(customerForm.getEmail());
                    personDto.setName(customerForm.getName());
                    personDto.setPhone(customerForm.getPhone());
                    personDto = personService.insert(personDto);
                }
            } else if (customerType == 1) {
                personDto.setName(customerForm.getName());
                personDto = personService.insert(personDto);
            }

            System.out.println(personDto);

            if(isPresent) {
                Integer id = personDto.getId();
                personDto = modelMapper.map(customerForm, PersonDto.class);
                personDto.setId(id);
                personDto = personService.updateById(personDto, id); 
            }

            customerDto.setIdCustomerType(customerType);
            customerDto.setPaymentDay(customerForm.getPaymentDay());
            customerDto.setIdPerson(personDto.getId());

            if (customerRepository.existsByIdPerson(personDto.getId())) {
                CustomerDto oldCustomer = this.findByIdPerson(personDto.getId());
                System.out.println("Old Customer: " + oldCustomer);
                System.out.println("Old Customer isActive: " + oldCustomer.getIsActive());

                if(oldCustomer.getIsActive() == false) {
                    
                    customerDto.setIsActive(true);
                    customerDto = this.updateById(customerDto, oldCustomer.getId());
                    
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

    public CustomerMensalDto updateCustomer(CustomerMensalDto customerMensalDto, Integer id) {
        try {
            CustomerDto customerDto = this.findById(id);
            PersonDto personDto = personService.findById(customerDto.getIdPerson());

            if (!customerMensalDto.getCpf().equals(personDto.getCpf())
                    && !customerMensalDto.getEmail().equals(personDto.getEmail())) {
                personService.validCpfAndEmail(customerMensalDto.getCpf(), customerMensalDto.getEmail());
            }

            personDto.setName(customerMensalDto.getName());
            personDto.setCpf(customerMensalDto.getCpf());
            personDto.setEmail(customerMensalDto.getEmail());
            personDto.setPhone(customerMensalDto.getPhone());
            customerDto.setIdCustomerType(customerMensalDto.getClientType());
            customerDto.setPaymentDay(customerMensalDto.getPaymentDay());

            personDto = personService.updateById(personDto, personDto.getId());
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
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);
            } else {
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Cliente!");
        }
    }

    public Page<CustomerGetDto> getCustomersByCustomerType(Integer customerType, Optional<Pageable> optionalPage) {
        try {
            Pageable page = optionalPage.orElse(Pageable.unpaged());
            Page<CustomerModel> customers = customerRepository.findByIdCustomerTypeAndIsActive(customerType, true,
                    page);
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
