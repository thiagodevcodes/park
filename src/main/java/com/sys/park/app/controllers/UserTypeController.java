package com.sys.park.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.UserType.UserTypeDto;
import com.sys.park.app.dtos.UserType.UserTypeForm;
import com.sys.park.app.services.UserTypeService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user_types")
public class UserTypeController {
    @Autowired
    UserTypeService userTypeService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<UserTypeDto>> findAll() {
        List<UserTypeDto> customerTypeDtoList = userTypeService.findAll();
        return ResponseEntity.ok().body(customerTypeDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTypeDto> find(@PathVariable("id") Integer id) {        
        UserTypeDto customerTypeDto = userTypeService.findById(id);
        return ResponseEntity.ok().body(customerTypeDto);
    }

    @PostMapping
    public ResponseEntity<UserTypeDto> insert(@Valid @RequestBody UserTypeForm userTypeForm, BindingResult br) {    
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        UserTypeDto customerTypeDto = userTypeService.insert(modelMapper.map(userTypeForm, UserTypeDto.class));
        return ResponseEntity.ok().body(customerTypeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserTypeDto> updateById(@Valid @RequestBody
        UserTypeForm costumerTypeForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        UserTypeDto costumerTypeDto = userTypeService.updateById(costumerTypeForm, id);
        return ResponseEntity.ok().body(costumerTypeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        userTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
