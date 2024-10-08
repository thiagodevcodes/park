package com.sys.park.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.User.UserDto;
import com.sys.park.app.dtos.User.CreateUserRequest;
import com.sys.park.app.dtos.User.UpdateUserRequest;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.repositories.RoleRepository;
import com.sys.park.app.repositories.UserRepository;
import com.sys.park.app.services.UserService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired 
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserModel> newUser(@Valid @RequestBody CreateUserRequest dto) {
        UserModel user = userService.newUser(dto);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UserDto> usersPage = userService.findAll(pageable);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("find")
    public ResponseEntity<UserDto> findById(@RequestParam("id") Long id) {
        var users = userService.findById(id);
        return ResponseEntity.ok(users);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDto> updateById(@Valid @RequestBody
        UpdateUserRequest userRequest, @RequestParam("id") Long id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        UserDto personDto = userService.updateUser(userRequest, id);
        return ResponseEntity.ok().body(personDto);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteById(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
