package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.User.UserDto;
import com.sys.park.app.dtos.User.UserForm;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.UserRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    public UserDto findById(Integer id) {
        try {
            UserModel acaoModel = userRepository.findById(id).get();
            return modelMapper.map(acaoModel, UserDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + UserModel.class.getName());
        }
    }

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

     public UserDto insert(UserForm userForm) {
        try {
            UserModel newUser = modelMapper.map(userForm, UserModel.class);
           
            Optional<UserModel> byLogin = userRepository.findByLogin(newUser.getLogin());
            
            if (byLogin.isPresent()) {
                throw new DataIntegrityException("Usuário já registrado.");
            }
            
            newUser = userRepository.save(newUser);
            return modelMapper.map(newUser, UserDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Usuário não foi(foram) preenchido(s).");
        }
    }

    public UserDto updateById(UserForm userForm, Integer id) {
        try {
            Optional<UserModel> userExist = userRepository.findById(id);

            if (userExist.isPresent()) {
                UserModel userUpdated = userExist.get();

                modelMapper.map(userForm, userUpdated);
                userUpdated = userRepository.save(userUpdated);

                return modelMapper.map(userUpdated, UserDto.class);
            }else{
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

            }else {
                throw new DataIntegrityException("O Id do Usuário não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Usuário!");
        }
    }
}
