package com.sys.park.app.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sys.park.app.models.PersonModel;
import com.sys.park.app.models.RoleModel;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.repositories.RoleRepository;
import com.sys.park.app.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        PersonModel personModel;

        if(!roleRepository.existsByName("ADMIN")) {
            var addRoleAdmin = new RoleModel();
            addRoleAdmin.setName("ADMIN");
            roleRepository.save(addRoleAdmin);  
        }

        if(!roleRepository.existsByName("BASIC")) {
            var addRoleBasic = new RoleModel();
            addRoleBasic.setName("BASIC");
            roleRepository.save(addRoleBasic);  
        }

        RoleModel role = roleRepository.findByName("ADMIN").get();
        Optional<UserModel> userAdmin = userRepository.findByUsername("admin");
        PersonModel person = new PersonModel();

        person.setName("Admin");
        personModel = personRepository.save(person);

        if(userAdmin != null) {
            userAdmin.ifPresentOrElse(
                (user) -> {
                    System.out.println("Admin já existe");
                },
                () -> {
                    var user = new UserModel();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setIdRole(role.getId());
                    user.setIdPerson(personModel.getId());
                    userRepository.save(user);
                });
        }
    }
}
