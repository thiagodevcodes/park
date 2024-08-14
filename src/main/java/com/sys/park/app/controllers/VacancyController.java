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

import com.sys.park.app.dtos.Vacancy.VacancyDto;
import com.sys.park.app.dtos.Vacancy.VacancyForm;
import com.sys.park.app.services.VacancyService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {
    @Autowired
    VacancyService vacancyService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<VacancyDto>> findAll() {
        List<VacancyDto> vacancyDto = vacancyService.findAll();
        return ResponseEntity.ok().body(vacancyDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacancyDto> findById(@PathVariable("id") Long id) {        
        VacancyDto vacancyDto = vacancyService.findById(id);
        return ResponseEntity.ok().body(vacancyDto);
    }

    @PostMapping
    public ResponseEntity<VacancyDto> insert(@Valid @RequestBody VacancyForm vacancyForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        VacancyDto vacancyDto = vacancyService.insert(modelMapper.map(vacancyForm, VacancyDto.class));
        return ResponseEntity.ok().body(vacancyDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyDto> updateById(@Valid @RequestBody
        VacancyForm vacancyForm, @PathVariable("id") Long id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        VacancyDto vacancyUpdated = vacancyService.updateById(modelMapper.map(vacancyForm, VacancyDto.class), id);
        return ResponseEntity.ok().body(vacancyUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        vacancyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
