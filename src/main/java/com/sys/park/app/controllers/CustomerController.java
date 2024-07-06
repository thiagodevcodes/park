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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.Customer.CustomerForm;
import com.sys.park.app.dtos.Customer.CustomerFormUpdate;
import com.sys.park.app.services.CustomerService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        List<CustomerDto> customerDtoList = customerService.findAll();
        return ResponseEntity.ok().body(customerDtoList);
    }

    @GetMapping("find")
    public ResponseEntity<CustomerDto> findById(@RequestParam("id") Integer id) {        
        CustomerDto customerDto = customerService.findById(id);
        return ResponseEntity.ok().body(customerDto);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> insert(@Valid @RequestBody CustomerForm customerForm, BindingResult br) {       
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        CustomerDto customerDto = customerService.addCustomer(customerForm);
        return ResponseEntity.ok().body(customerDto);
    }

    @PutMapping
    public ResponseEntity<CustomerDto> updateById(@Valid @RequestBody
        CustomerFormUpdate customerForm, @RequestParam("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        CustomerDto costumerDto = customerService.updateCustomer(customerForm, id);
        return ResponseEntity.ok().body(costumerDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(@RequestParam("id") Integer id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

