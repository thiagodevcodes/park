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
import com.sys.park.app.dtos.Customer.CustomerRequest;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Person.PersonRequest;
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

    public CustomerDto findById(Long id) {
        try {
            CustomerModel customerModel = customerRepository.findById(id).get();
            return modelMapper.map(customerModel, CustomerDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + CustomerDto.class.getName());
        }
    }

    public CustomerDto updateById(CustomerRequest customerRequest, Long id) {
        try {
            Optional<CustomerModel> customerExist = customerRepository.findById(id);      

            if (customerExist.isPresent()) {
                CustomerModel customerUpdated = customerExist.get();
                
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(customerRequest, customerUpdated);
                customerUpdated = this.customerRepository.save(customerUpdated);
                
                return modelMapper.map(customerUpdated, CustomerDto.class);
            } else {
                throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível atualizar o cliente!");
        }
    }

    public void deleteById(Long id) {
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

    public CustomerDto createDataCustomer(CustomerRequest customerForm, PersonRequest personForm) {
        PersonModel person = new PersonModel();
        CustomerModel customer = new CustomerModel();

        if(personRepository.existsByCpf(personForm.getCpf())) {
            person = personRepository.findByCpf(personForm.getCpf()).get();
            System.out.println("Person que existe: " + person);
        } else {
            person.setCpf(personForm.getCpf());
        }
  
        person.setEmail(personForm.getEmail());                      
        person.setPhone(personForm.getPhone());
        person.setName(personForm.getName());

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

        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        customerDto.setPerson(modelMapper.map(person, PersonDto.class));
        return customerDto;
    }

    public CustomerDto insert(CustomerModel customerModel) {
        try {      
            CustomerModel newCustomer = customerRepository.save(customerModel);
            
            return modelMapper.map(newCustomer, CustomerDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Pessoa não foi(foram) preenchido(s).");
        }
    }

    public CustomerDto addCustomer(CustomerRequest customerRequest) {
        PersonModel person = new PersonModel();
        CustomerModel customer = new CustomerModel();
        PersonRequest personForm = customerRequest.getPerson();

        if(this.personRepository.existsByCpf(personForm.getCpf()) && !personForm.getCpf().equals(null)) {
            person = personRepository.findByCpf(personForm.getCpf()).get();
            personForm = modelMapper.map(person, PersonRequest.class);
            
            if(customerRepository.existsByIdPerson(person.getId())) {
                customer = customerRepository.findByIdPerson(person.getId()).get();
                customerRequest = modelMapper.map(customer, CustomerRequest.class);

                if(customer.getIsActive().equals(true)) {
                    throw new DataIntegrityException("Cliente com este CPF já existe");
                }
            } 
        } else {
            personService.validCpf(personForm.getCpf());
            personService.validEmail(personForm.getEmail());
            //personService.validPerson(modelMapper.map(personForm, PersonDto.class));
        }


        CustomerDto customerDto = createDataCustomer(customerRequest, personForm);
        return customerDto;
    }

    public CustomerDto updateCustomer(CustomerRequest customerRequest, Long id) {
        Optional<CustomerModel> customerOld = customerRepository.findById(id);
        PersonRequest personForm = customerRequest.getPerson();
        
        if (customerOld.isPresent()) {
            CustomerModel customerExist = customerOld.get();
            PersonModel personOld = personRepository.findById(customerExist.getIdPerson()).get();

            personService.updateById(modelMapper.map(personForm, PersonRequest.class), customerExist.getIdPerson());
            CustomerDto customerDto = this.updateById(customerRequest, id);
            customerDto.setPerson(modelMapper.map(personOld, PersonDto.class));
            return customerDto;
        } else {
            throw new DataIntegrityException("O Id do Cliente não existe na base de dados!");
        }
    }
}
