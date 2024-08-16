package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Person.PersonRequest;
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

    public PersonDto findById(Long id) {
        try {
            PersonModel personModel = personRepository.findById(id).get();
            return modelMapper.map(personModel, PersonDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public PersonDto insert(PersonModel personModel) {
        try {       
            this.validCpf(personModel.getCpf());
            this.validEmail(personModel.getEmail());
            //this.validPerson(modelMapper.map(personForm, PersonDto.class));

            PersonModel newPerson = personRepository.save(personModel);
            
            return modelMapper.map(newPerson, PersonDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Pessoa não foi(foram) preenchido(s).");
        }
    }

    public PersonDto updateById(PersonRequest personForm, Long id ) {
        try {
            Optional<PersonModel> personExist = personRepository.findById(id);
            
            if (personExist.isPresent()) {
                PersonModel personUpdated = personExist.get();
               
                if (!Objects.equals(personUpdated.getCpf(), personForm.getCpf())) {
                    this.validCpf(personForm.getCpf());
                }
                
                if (!Objects.equals(personUpdated.getEmail(), personForm.getEmail())) {
                    this.validEmail(personForm.getEmail());
                }
                
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(personForm, personUpdated);
                personUpdated = personRepository.save(personUpdated);

                return modelMapper.map(personUpdated, PersonDto.class);
            }else{
                throw new DataIntegrityException("O Id da Pessoa não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Pessoa não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Long id) {
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



    public Boolean validCpf(String cpf) {
        if (personRepository.existsByCpf(cpf) && cpf != null) {
            throw new DataIntegrityException("CPF já cadastrado");
        }
        return true;
    }

    public Boolean validEmail(String email) {
        if (personRepository.existsByEmail(email) && email != null) {
            throw new DataIntegrityException("Email já cadastrado");
        }
        return true;
    }
}

