package com.sys.park.app.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<CustomerDto> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).map(user -> {
            CustomerDto customerDto = modelMapper.map(user, CustomerDto.class);

            Optional<PersonModel> personModelOptional = personRepository.findById(user.getIdPerson());
            if (personModelOptional.isPresent()) {
                PersonModel personModel = personModelOptional.get();
                PersonDto personDto = modelMapper.map(personModel, PersonDto.class);
                customerDto.setPerson(personDto);
            } else {
                throw new BusinessRuleException("Cliente não encontrado!");
            }

            return customerDto;
        });
    }

    public CustomerDto findById(Long id) {
        try {
            CustomerModel customerModel = customerRepository.findById(id).get();
            PersonDto personDto = personService.findById(customerModel.getIdPerson());

            CustomerDto customerResponseDto= modelMapper.map(customerModel, CustomerDto.class);
            customerResponseDto.setPerson(personDto);

            return customerResponseDto;
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

    public CustomerDto insert(CustomerModel customerModel) {
        try {      
            if(customerRepository.existsByIdPerson(customerModel.getIdPerson())) {
                throw new DataIntegrityException("Pessoa já existe");
            }

            CustomerModel newCustomer = customerRepository.save(customerModel);
            
            return modelMapper.map(newCustomer, CustomerDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Pessoa não foi(foram) preenchido(s).");
        }
    }

    public CustomerDto addCustomer(CustomerRequest customerRequest) {
        PersonRequest personForm = customerRequest.getPerson();

        CustomerModel customerModel = modelMapper.map(customerRequest, CustomerModel.class);
        PersonModel personModel = modelMapper.map(personForm, PersonModel.class);
        
        customerModel.setIsActive(true);
        PersonDto personDto = personService.insert(personModel);
        
        customerModel.setIdPerson(personDto.getId());
        CustomerDto customerDto = this.insert(customerModel);

        // CustomerDto customerDto = createDataCustomer(customerRequest, personForm);
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
