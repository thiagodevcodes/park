package com.sys.park.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Person.PersonForm;
import com.sys.park.app.dtos.Person.PersonUpdateForm;
import com.sys.park.app.services.PersonService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/persons")
public class PersonController {
    @Autowired
    PersonService personService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PersonDto>> findAll() {
        List<PersonDto> userDtoList = personService.findAll();
        return ResponseEntity.ok().body(userDtoList);
    }

    @GetMapping("find")
    public ResponseEntity<PersonDto> findById(@RequestParam("id") Integer id) {        
        PersonDto personDto = personService.findById(id);
        return ResponseEntity.ok().body(personDto);
    }

    @PostMapping
    public ResponseEntity<PersonDto> insert(@Valid @RequestBody PersonForm personForm, BindingResult br) {
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        PersonDto personDto = personService.insert(personForm);
        return ResponseEntity.ok().body(personDto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDto> updateById(@Valid @RequestBody
        PersonUpdateForm personForm, @RequestParam("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        PersonDto personDto = personService.updateById(personForm, id);
        return ResponseEntity.ok().body(personDto);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@RequestParam("id") Integer id) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
