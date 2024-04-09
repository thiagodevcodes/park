package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Person.PersonForm;
import com.sys.park.app.models.PersonModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    public PersonDto findById(Integer id) {
        try {
            PersonModel personModel = personRepository.findById(id).get();
            return modelMapper.map(personModel, PersonDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public List<PersonDto> findAll() {
        try {
            List<PersonModel> personModelList = personRepository.findAll();

            return personModelList.stream()
                    .map(user -> modelMapper.map(user, PersonDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar a Pessoa!", e.getErrorMessages());
        }
    }

    public PersonDto insert(PersonDto personDto, Integer customerType) {
        try {
            PersonModel newPerson = modelMapper.map(personDto, PersonModel.class);
            
            if(customerType == 2) {
                Optional<PersonModel> cpfExist = personRepository.findByCpf(personDto.getCpf());
                Optional<PersonModel> emailExist = personRepository.findByEmail(personDto.getEmail());

                if(cpfExist.isPresent()) {
                    throw new DataIntegrityException("CPF já cadastrado!.");
                }
    
                if(emailExist.isPresent()) {
                    throw new DataIntegrityException("Email já cadastrado!.");
                }
            }

            newPerson = personRepository.save(newPerson);
            return modelMapper.map(newPerson, PersonDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Pessoa não foi(foram) preenchido(s).");
        }
    }

    public PersonDto updateById(PersonForm userForm, Integer id) {
        try {
            Optional<PersonModel> personExist = personRepository.findById(id);

            if (personExist.isPresent()) {
                PersonModel personUpdated = personExist.get();

                modelMapper.map(userForm, personUpdated);
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

    public PersonDto findByCpf(String cpf) {
        try {
            PersonModel personModel = personRepository.findByCpf(cpf).get();
            return modelMapper.map(personModel, PersonDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + cpf + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public Boolean verifyCpfAndEmail(String cpf, String email) {
        try {
            Optional<PersonModel> cpfExist = personRepository.findByCpf(cpf);
            Optional<PersonModel> emailExist = personRepository.findByEmail(email);

            if(cpfExist.isPresent() || emailExist.isPresent()) {
                return true;
            } else {
                return false;
            }

        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + cpf + ", Tipo: " + PersonModel.class.getName());
        }
    }
}

