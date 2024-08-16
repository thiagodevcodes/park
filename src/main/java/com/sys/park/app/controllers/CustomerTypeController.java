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

import com.sys.park.app.dtos.CustomerType.CustomerTypeDto;
import com.sys.park.app.dtos.CustomerType.CustomerTypeRequest;
import com.sys.park.app.services.CustomerTypeService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer_type")
public class CustomerTypeController {
    @Autowired
    CustomerTypeService customerTypeService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CustomerTypeDto>> findAll() {
        List<CustomerTypeDto> customerTypeDtoList = customerTypeService.findAll();
        return ResponseEntity.ok().body(customerTypeDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerTypeDto> find(@PathVariable("id") Long id) {        
        CustomerTypeDto customerTypeDto = customerTypeService.findById(id);
        return ResponseEntity.ok().body(customerTypeDto);
    }

    @PostMapping
    public ResponseEntity<CustomerTypeDto> insert(@Valid @RequestBody CustomerTypeRequest customerTypeForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        CustomerTypeDto customerTypeDto = customerTypeService.insert(modelMapper.map(customerTypeForm, CustomerTypeDto.class));
        return ResponseEntity.ok().body(customerTypeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerTypeDto> updateById(@Valid @RequestBody
        CustomerTypeRequest costumerTypeForm, @PathVariable("id") Long id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        CustomerTypeDto costumerTypeDto = customerTypeService.updateById(costumerTypeForm, id);
        return ResponseEntity.ok().body(costumerTypeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        customerTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
