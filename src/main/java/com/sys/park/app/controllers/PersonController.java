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

import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Person.PersonMensalista;
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

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> find(@PathVariable("id") Integer id) {        
        PersonDto personDto = personService.findById(id);
        return ResponseEntity.ok().body(personDto);
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> findAll() {
        List<PersonDto> userDtoList = personService.findAll();
        return ResponseEntity.ok().body(userDtoList);
    }

    @PostMapping
    public ResponseEntity<PersonDto> insert(@Valid @RequestBody PersonMensalista personForm, BindingResult br) {
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Restrição de Dados", errors);
        }

        PersonDto personDto = personService.insert(modelMapper.map(personForm, PersonDto.class), personForm.getClientType());
        return ResponseEntity.ok().body(personDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@Valid @RequestBody
        PersonMensalista personForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Restrição de Dados", errors);
        }
     
        PersonDto personDto = personService.updateById(personForm, id);
        return ResponseEntity.ok().body(personDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
