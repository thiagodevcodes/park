package com.sys.park.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.User.CreateUserDto;
import com.sys.park.app.models.PersonModel;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.repositories.RoleRepository;
import com.sys.park.app.repositories.UserRepository;
import com.sys.park.app.services.exceptions.DataIntegrityException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public UserModel newUser(CreateUserDto user) {
        var roleDto = roleRepository.findById(user.role());

        var userFromDb = userRepository.findByUsername(user.username());
        
        if (userFromDb.isPresent()) {
            throw new DataIntegrityException("Usuário já existe");
        }

        PersonModel person = new PersonModel();
        person.setName(user.name());
        person.setCpf(user.cpf());
        person.setEmail(user.email());
        person.setPhone(user.phone());

        personRepository.save(person);

        UserModel userModel = new UserModel();
        userModel.setUsername(user.username());
        userModel.setPassword(passwordEncoder.encode(user.password()));
        userModel.setIdRole(roleDto.get().getId());
        userModel.setIdPerson(person.getId());

        userRepository.save(userModel);

        return userModel;
    }
}
