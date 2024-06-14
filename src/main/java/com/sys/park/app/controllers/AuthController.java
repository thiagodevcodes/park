package com.sys.park.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.Auth.AuthenticationDto;
import com.sys.park.app.dtos.Auth.LoginResponseDto;
import com.sys.park.app.dtos.Auth.RegisterForm;
import com.sys.park.app.dtos.User.UserDto;
import com.sys.park.app.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDto data) {
        LoginResponseDto login = userService.loginUser(data);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterForm data) {
        UserDto newUser = userService.addNewUser(data);
        return ResponseEntity.ok().body(newUser);
    }
}
