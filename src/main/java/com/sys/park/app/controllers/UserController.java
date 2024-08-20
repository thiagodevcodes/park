package com.sys.park.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.User.CreateUserDto;
import com.sys.park.app.dtos.User.UserDto;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.repositories.RoleRepository;
import com.sys.park.app.repositories.UserRepository;
import com.sys.park.app.services.UserService;

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
    public ResponseEntity<UserModel> newUser(@Valid @RequestBody CreateUserDto dto) {
        UserModel user = userService.newUser(dto);
        return ResponseEntity.ok().body(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers() {
        var users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("find")
    public ResponseEntity<UserModel> findById(@RequestParam("id") Long id) {
        var users = userRepository.findById(id).get();
        return ResponseEntity.ok(users);
    }
}
