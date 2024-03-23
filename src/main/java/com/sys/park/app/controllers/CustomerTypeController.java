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

import com.sys.park.app.dtos.CustomerType.CustomerTypeDto;
import com.sys.park.app.dtos.CustomerType.CustomerTypeForm;
import com.sys.park.app.services.CustomerTypeService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer_type")
public class CustomerTypeController {
    @Autowired
    CustomerTypeService customerTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerTypeDto> find(@PathVariable("id") Integer id) {        
        CustomerTypeDto customerTypeDto = customerTypeService.findById(id);
        return ResponseEntity.ok().body(customerTypeDto);
    }

    @GetMapping
    public ResponseEntity<List<CustomerTypeDto>> findAll() {
        List<CustomerTypeDto> customerTypeDtoList = customerTypeService.findAll();
        return ResponseEntity.ok().body(customerTypeDtoList);
    }

    @PostMapping
    public ResponseEntity<CustomerTypeDto> insert(@Valid @RequestBody CustomerTypeForm customerTypeForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Restrição de Dados", errors);
        }

        CustomerTypeDto customerTypeDto = customerTypeService.insert(customerTypeForm);
        return ResponseEntity.ok().body(customerTypeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerTypeDto> update(@Valid @RequestBody
        CustomerTypeForm costumerTypeForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Restrição de Dados", errors);
        }
     
        CustomerTypeDto costumerTypeDto = customerTypeService.updateById(costumerTypeForm, id);
        return ResponseEntity.ok().body(costumerTypeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        customerTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
