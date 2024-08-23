package com.sys.park.app.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.User.UserDto;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.User.CreateUserRequest;
import com.sys.park.app.dtos.User.UpdateUserRequest;
import com.sys.park.app.models.PersonModel;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.repositories.RoleRepository;
import com.sys.park.app.repositories.UserRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonService personService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);

            Optional<PersonModel> personModelOptional = personRepository.findById(user.getIdPerson());
            if (personModelOptional.isPresent()) {
                PersonModel personModel = personModelOptional.get();
                PersonDto personDto = modelMapper.map(personModel, PersonDto.class);
                userDto.setPerson(personDto);
            } else {
                throw new BusinessRuleException("Pessoa não encontrada!");
            }

            return userDto;
        });
    }

    public UserDto findById(Long id) {
        try {
            UserModel userModel = userRepository.findById(id).get();
            PersonDto personDto = personService.findById(userModel.getIdPerson());

            UserDto userResponseDto = modelMapper.map(userModel, UserDto.class);
            userResponseDto.setPerson(personDto);

            return userResponseDto;
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public UserModel newUser(CreateUserRequest user) {
        var roleDto = roleRepository.findById(user.getRole());

        var userFromDb = userRepository.findByUsername(user.getUsername());
        
        if (userFromDb.isPresent()) {
            throw new DataIntegrityException("Usuário já existe");
        }

        PersonModel personModel = modelMapper.map(user.getPerson(), PersonModel.class);
        personService.insert(personModel);

        UserModel userModel = new UserModel();
        userModel.setUsername(user.getUsername());
        userModel.setPassword(passwordEncoder.encode(user.getPassword()));
        userModel.setIdRole(roleDto.get().getId());
        userModel.setIdPerson(personModel.getId());

        userRepository.save(userModel);

        return userModel;
    }


    public UserDto updateById(UpdateUserRequest user, Long id ) {
        try {
            Optional<UserModel> userExist = userRepository.findById(id);
            
            if (userExist.isPresent()) {
                UserModel userUpdated = userExist.get();
                
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(user, userUpdated);
                userUpdated = userRepository.save(userUpdated);

                return modelMapper.map(userUpdated, UserDto.class);
            }else{
                throw new DataIntegrityException("O Id do Usuário não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Pessoa não foi(foram) preenchido(s).");
        }
    }

}
