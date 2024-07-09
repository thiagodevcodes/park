package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.Customer.CustomerForm;
import com.sys.park.app.dtos.Customer.CustomerFormUpdate;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Person.PersonForm;
import com.sys.park.app.dtos.Person.PersonUpdateForm;
import com.sys.park.app.models.CustomerModel;
import com.sys.park.app.models.PersonModel;
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
    private ModelMapper modelMapper;

    public List<CustomerDto> findAll() {
        try {
            List<CustomerModel> customerModelList = customerRepository.findAll();
            
            return customerModelList.stream()
                    .map(customer -> {
                        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
                        // Aqui você adiciona o atributo person ao CustomerDto
                        PersonModel person = personRepository.findById(customer.getIdPerson()).get();
                        customerDto.setPerson(modelMapper.map(person, PersonDto.class)); // Supondo que getPerson() retorna o atributo person
                        return customerDto;
                    })
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

    public CustomerDto updateById(CustomerFormUpdate customerFormUpdate, Integer id) {
        try {
            Optional<CustomerModel> customerExist = customerRepository.findById(id);      

            if (customerExist.isPresent()) {
                CustomerModel customerUpdated = customerExist.get();
                
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(customerFormUpdate, customerUpdated);
                customerUpdated = this.customerRepository.save(customerUpdated);
                
                return modelMapper.map(customerUpdated, CustomerDto.class);
            } else {
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível atualizar o cliente!");
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

    public CustomerDto createDataCustomer(CustomerForm customerForm, PersonForm personForm) {
        PersonModel person = new PersonModel();
        CustomerModel customer = new CustomerModel();

        System.out.println(personForm);

        if(personRepository.existsByCpf(personForm.getCpf())) {
            person = personRepository.findByCpf(personForm.getCpf()).get();
            System.out.println("Person que existe: " + person);
        } else {
            person.setCpf(personForm.getCpf());
        }
  
        person.setEmail(personForm.getEmail());                      
        person.setPhone(personForm.getPhone());
        person.setName(personForm.getName());
        System.out.println(person);

        person = personRepository.save(person);

        customer.setIsActive(true);
        customer.setIdPerson(person.getId());
        customer.setPaymentDay(customerForm.getPaymentDay());
        customer.setIdCustomerType(customerForm.getIdCustomerType());

        if(customerRepository.existsByIdPerson(customer.getIdPerson())) {
            customer = this.customerRepository.findByIdPerson(customer.getIdPerson()).get();
        } else {
            customer = this.customerRepository.save(customer);
        }

        System.out.println(customer);


        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        customerDto.setPerson(modelMapper.map(person, PersonDto.class));
        return customerDto;
    }

    public CustomerDto addCustomer(CustomerForm customerForm) {
        PersonModel person = new PersonModel();
        CustomerModel customer = new CustomerModel();
        PersonForm personForm = customerForm.getPerson();

        if(this.personRepository.existsByCpf(personForm.getCpf()) && !personForm.getCpf().equals(null)) {
            person = personRepository.findByCpf(personForm.getCpf()).get();
            personForm = modelMapper.map(person, PersonForm.class);
            
            if(customerRepository.existsByIdPerson(person.getId())) {
                customer = customerRepository.findByIdPerson(person.getId()).get();
                customerForm = modelMapper.map(customer, CustomerForm.class);

                if(customer.getIsActive().equals(true)) {
                    throw new DataIntegrityException("Cliente com este CPF já existe");
                }
            } 
        } else {
            personService.validCpf(personForm.getCpf());
            personService.validEmail(personForm.getEmail());
            //personService.validPerson(modelMapper.map(personForm, PersonDto.class));
        }


        CustomerDto customerDto = createDataCustomer(customerForm, personForm);
        return customerDto;
    }

    public CustomerDto updateCustomer(CustomerFormUpdate customerForm, Integer id) {
        Optional<CustomerModel> customerOld = customerRepository.findById(id);
        PersonForm personForm = customerForm.getPerson();
        
        if (customerOld.isPresent()) {
            CustomerModel customerExist = customerOld.get();
            PersonModel personOld = personRepository.findById(customerExist.getIdPerson()).get();

            personService.updateById(modelMapper.map(personForm, PersonUpdateForm.class), customerExist.getIdPerson());
            CustomerDto customerDto = this.updateById(customerForm, id);
            customerDto.setPerson(modelMapper.map(personOld, PersonDto.class));
            return customerDto;
        } else {
            throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
        }
    }
}
