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

import com.sys.park.app.dtos.Customer.CustomerForm;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.models.PersonModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    public List<PersonDto> findAll() {
        try {
            List<PersonModel> personModelList = personRepository.findAll();

            return personModelList.stream()
                    .map(user -> modelMapper.map(user, PersonDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar a Pessoa!");
        }
    }

    public PersonDto findById(Integer id) {
        try {
            PersonModel personModel = personRepository.findById(id).get();
            return modelMapper.map(personModel, PersonDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public PersonDto findByCpf(String cpf) {
        try {
            PersonModel personModel = personRepository.findByCpf(cpf).get();
            return modelMapper.map(personModel, PersonDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Cpf: " + cpf + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public PersonDto findByEmail(String email) {
        try {
            PersonModel personModel = personRepository.findByEmail(email).get();
            return modelMapper.map(personModel, PersonDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Email: " + email + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public PersonDto findByPhone(String phone) {
        try {
            PersonModel personModel = personRepository.findByPhone(phone).get();
            return modelMapper.map(personModel, PersonDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Telefone: " + phone + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public PersonDto insert(PersonDto personDto) {
        try {
            PersonModel newPerson = modelMapper.map(personDto, PersonModel.class);
            newPerson = personRepository.save(newPerson);
            return modelMapper.map(newPerson, PersonDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Pessoa não foi(foram) preenchido(s).");
        }
    }

    public PersonDto updateById(PersonDto personDto, Integer id) {
        try {
            Optional<PersonModel> personExist = personRepository.findById(id);

            if (personExist.isPresent()) {
                PersonModel personUpdated = personExist.get();

                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(personDto, personUpdated);
                personUpdated = personRepository.save(personUpdated);

                return modelMapper.map(personUpdated, PersonDto.class);
            }else{
                throw new DataIntegrityException("O Id da Pessoa não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Pessoa não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            if (personRepository.existsById(id)) {
                personRepository.deleteById(id);

            }else {
                throw new DataIntegrityException("O Id da Pessoa não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir a Pessoa!");
        }
    }

    public PersonDto getPersonByCustomer(CustomerForm customerForm) {
        try {
            PersonDto personDto = new PersonDto();


            if(customerForm.getClientType().equals(2)) {
                personDto.setCpf(customerForm.getCpf());
                personDto.setEmail(customerForm.getEmail());
                personDto.setName(customerForm.getName());
                personDto.setPhone(customerForm.getPhone());

                if (personRepository.existsByEmail(customerForm.getEmail())) {
                    PersonDto person = this.findByEmail(customerForm.getEmail()); 
                    System.out.println(person);
                    System.out.println(personDto);
                    personDto = this.updateById(personDto, person.getId());
                    
                } else if (personRepository.existsByCpf(customerForm.getCpf())) {
                    PersonDto person = this.findByCpf(customerForm.getCpf());
                    System.out.println(person);
                    personDto = this.updateById(personDto, person.getId());
                } else {
                    personDto = this.insert(personDto);
                } 
            } else {
                personDto.setName(customerForm.getName());
                personDto = this.insert(personDto);
            }
        
            return personDto;
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + customerForm.getEmail() + ", Tipo: " + PersonModel.class.getName());
        }
    }
}

