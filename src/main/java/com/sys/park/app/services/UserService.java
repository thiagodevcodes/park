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

import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.User.UserDto;
import com.sys.park.app.dtos.User.UserForm;
import com.sys.park.app.dtos.User.UserFormUpdate;
import com.sys.park.app.dtos.User.UserGetDto;
import com.sys.park.app.dtos.UserType.UserTypeDto;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.repositories.UserRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserDto> findAll() {
        try {
            List<UserModel> userModelList = userRepository.findAll();

            return userModelList.stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar os Usuários!");
        }
    }

    public UserDto findById(Integer id) {
        try {
            UserModel userModel = userRepository.findById(id).get();
            return modelMapper.map(userModel, UserDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + UserModel.class.getName());
        }
    }

    public UserDto findByIdPerson(Integer idPerson) {
        try {
            UserModel userModel = userRepository.findByIdPerson(idPerson).get();
            return modelMapper.map(userModel, UserDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException(
                    "Objeto não encontrado! Id Pessoa: " + idPerson + ", Tipo: " + UserModel.class.getName());
        }
    }

    public Boolean userByIdPerson(Integer idPerson) {
        try {
            Optional<UserModel> userModel = userRepository.findByIdPerson(idPerson);

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

    public UserDto insert(UserDto userDto) {
        try {
            UserModel newUser = modelMapper.map(userDto, UserModel.class);

            Optional<UserModel> byLogin = userRepository.findByUsername(newUser.getUsername());

            if (byLogin.isPresent()) {
                throw new DataIntegrityException("Usuário já registrado.");
            }

            newUser = userRepository.save(newUser);
            return modelMapper.map(newUser, UserDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Usuário não foi(foram) preenchido(s).");
        }
    }

    public UserDto updateById(UserDto userForm, Integer id) {
        try {
            Optional<UserModel> userExist = userRepository.findById(id);

            if (userExist.isPresent()) {
                UserModel userUpdated = userExist.get();

                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(userForm, userUpdated);

                userUpdated = userRepository.save(userUpdated);

                return modelMapper.map(userUpdated, UserDto.class);
            } else {
                throw new DataIntegrityException("O Id do Usuário não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Usuário não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);

            } else {
                throw new DataIntegrityException("O Id do Usuário não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Usuário!");
        }
    }

    public Boolean validUser(UserForm user) {
        PersonDto newPerson = new PersonDto();
        UserDto userDto = new UserDto();
        Boolean isPresent = false;

        if (!user.getPassword().equals(user.getConfirmPassword())) 
            throw new DataIntegrityException("As senhas se diferem!");
        
        if (personRepository.existsByEmail(user.getEmail())) {
            newPerson = personService.findByEmail(user.getEmail());
            isPresent = true;
        } else if(personRepository.existsByPhone(user.getPhone())) {
            newPerson = personService.findByPhone(user.getPhone());
            isPresent = true;
        }

        if (isPresent) {
            if (this.userByIdPerson(newPerson.getId())) {
                userDto = this.findByIdPerson(newPerson.getId());
                if (userDto.getIsActive()) {    
                    if (userRepository.existsByUsername(user.getUsername())) 
                        throw new DataIntegrityException("Nome de usuário já utilizado!");

                    if(personRepository.existsByEmail(user.getEmail()))
                        throw new BusinessRuleException("O email já existe!");

                    if(personRepository.existsByPhone(user.getPhone())) 
                        throw new BusinessRuleException("O celular já existe!");
                }
            }
        }

        return isPresent;
    }

    public UserDto addNewUser(UserForm userForm) {
        try {
            this.validUser(userForm);
            UserDto userDto = new UserDto();
            PersonDto newPerson = new PersonDto();
            Boolean isPresent = false;
            
            if (personRepository.existsByEmail(userForm.getEmail())) {
                newPerson = personService.findByEmail(userForm.getEmail()); 
                isPresent = true; 
            } else if(personRepository.existsByPhone(userForm.getPhone())) {
                newPerson = personService.findByPhone(userForm.getPhone());  
                isPresent = true; 
            } else {
                newPerson.setName(userForm.getName());
                newPerson.setPhone(userForm.getPhone());
                newPerson.setEmail(userForm.getEmail());
                newPerson = personService.insert(newPerson);  
            }
 
            if(isPresent) {
                Integer id = newPerson.getId();
                newPerson = modelMapper.map(userForm,PersonDto.class);
                newPerson.setId(id);
                newPerson = personService.updateById(newPerson, id); 
            }

            userDto.setIdUserType(userForm.getUserType());
            userDto.setPassword(userForm.getPassword());
            userDto.setIsActive(true);
            userDto.setUserType(userForm.getUserType());
            userDto.setUsername(userForm.getUsername());
            userDto.setIdPerson(newPerson.getId());

            if (userRepository.existsByIdPerson(newPerson.getId())) {      
                UserDto oldUser = this.findByIdPerson(newPerson.getId());   

                if(oldUser.getIsActive() == false) {
                    userDto = this.updateById(userDto, oldUser.getId());
                } else {
                    throw new DataIntegrityException("Usuário já está ativo!");
                }       
            } else {
                userDto = this.insert(userDto);
            }

            return userDto;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Usuário não foi(foram) preenchido(s).");
        }
    }

    public Page<UserGetDto> getUsers(Optional<Pageable> optionalPage) {
        try {
            Pageable page = optionalPage.orElse(Pageable.unpaged());
            Page<UserModel> users = userRepository.findByIsActive(true, page);
            List<UserGetDto> newDtoList = new ArrayList<>();

            for (UserModel user : users) {
                UserGetDto newDto = new UserGetDto();

                PersonDto personDto = personService.findById(user.getIdPerson());
                UserTypeDto userTypeDto = userTypeService.findById(user.getIdUserType());

                if (personDto != null && userTypeDto != null && user.getIsActive()) {
                    newDto.setId(user.getId());
                    newDto.setEmail(personDto.getEmail());
                    newDto.setName(personDto.getName());
                    newDto.setPhone(personDto.getPhone());
                    newDto.setUsername(user.getUsername());
                    newDto.setUserType(userTypeDto.getId());
                    newDtoList.add(newDto);
                }
            }

            Page<UserGetDto> pageableClient = new PageImpl<>(newDtoList, page, users.getTotalElements());
            return pageableClient;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível buscar os Clientes!");
        }
    }

    public UserDto updateUser(UserFormUpdate userGetDto, Integer id) {
        try {
            UserDto userDto = this.findById(id);
            PersonDto personDto = personService.findById(userDto.getIdPerson());

            if (!userGetDto.getEmail().equals(personDto.getEmail())) {
                Boolean emailExist = personService.verifyEmail(userGetDto.getEmail());
                if (emailExist)
                    throw new DataIntegrityException("Email já existe!");
            }

            personDto.setName(userGetDto.getName());
            personDto.setEmail(userGetDto.getEmail());
            personDto.setPhone(userGetDto.getPhone());

            personDto = personService.updateById(personDto, personDto.getId());

            userDto = this.updateById(modelMapper.map(userGetDto, UserDto.class), id);

            UserGetDto updatedUser = new UserGetDto();

            updatedUser.setEmail(personDto.getEmail());
            updatedUser.setName(personDto.getName());
            updatedUser.setPhone(personDto.getPhone());
            updatedUser.setUsername(userDto.getUsername());
            updatedUser.setUserType(userDto.getUserType());

            return userDto;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não foi possível atualizar o cliente", e);
        }
    }

    public UserDto finishUser(UserDto userDto, Integer id) {
        try {
            Optional<UserModel> userExist = userRepository.findById(id);

            if (userExist.isPresent()) {
                UserModel user = userExist.get();

                user.setIsActive(false);

                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                userDto = modelMapper.map(user, UserDto.class);

                userDto = this.updateById(userDto, id);

                return userDto;
            } else {
                throw new DataIntegrityException("O Id do Ticket não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).", e);
        }
    }
}
