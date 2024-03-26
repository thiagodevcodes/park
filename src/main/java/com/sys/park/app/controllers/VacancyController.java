package com.sys.park.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.sys.park.app.dtos.Vacancy.VacancyDto;
import com.sys.park.app.dtos.Vacancy.VacancyForm;
import com.sys.park.app.services.VacancyService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vacancys")
public class VacancyController {
    @Autowired
    VacancyService vacancyService;

    @GetMapping("/{id}")
    public ResponseEntity<VacancyDto> find(@PathVariable("id") Integer id) {        
        VacancyDto vacancyDto = vacancyService.findById(id);
        return ResponseEntity.ok().body(vacancyDto);
    }

    @GetMapping
    public ResponseEntity<List<VacancyDto>> findAll() {
        List<VacancyDto> vacancyDtoList = vacancyService.findAll();
        return ResponseEntity.ok().body(vacancyDtoList);
    }

    @PostMapping
    public ResponseEntity<VacancyDto> insert(@Valid @RequestBody VacancyForm vacancyForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Restrição de Dados", errors);
        }

        VacancyDto vacancyDto = vacancyService.insert(vacancyForm);
        return ResponseEntity.ok().body(vacancyDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyDto> update(@Valid @RequestBody
        VacancyForm vacancyForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Restrição de Dados", errors);
        }
     
        VacancyDto vacancyDto = vacancyService.updateById(vacancyForm, id);
        return ResponseEntity.ok().body(vacancyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        vacancyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}