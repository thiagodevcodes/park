package com.sys.park.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.User.UserDto;
import com.sys.park.app.dtos.User.UserForm;
import com.sys.park.app.dtos.User.UserFormUpdate;
import com.sys.park.app.dtos.User.UserGetDto;
import com.sys.park.app.services.UserService;
import com.sys.park.app.services.exceptions.ConstraintException;
import com.sys.park.app.services.exceptions.DataIntegrityException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;
  
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtoList = userService.findAll();
        return ResponseEntity.ok().body(userDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> find(@PathVariable("id") Integer id) {        
        UserDto userDto = userService.findById(id);
        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<UserGetDto>> findAllMov(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        try {
            Pageable pageable = Pageable.unpaged();

            if (page != null && size != null) {
                pageable = PageRequest.of(page, size);
            }

            Page<UserGetDto> userDtoPage = userService.getUsers(Optional.of(pageable));
            return ResponseEntity.ok().body(userDtoPage);     
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro de paginação");
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> insert(@Valid @RequestBody UserForm userForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        UserDto userDto = userService.addNewUser(userForm);
        return ResponseEntity.ok().body(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateById(@Valid @RequestBody
        UserFormUpdate userForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        UserDto userDto = userService.updateUser(userForm, id);
        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
