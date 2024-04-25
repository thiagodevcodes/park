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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.Customer.CustomerForm;
import com.sys.park.app.dtos.Customer.CustomerMensalDto;
import com.sys.park.app.services.CustomerService;
import com.sys.park.app.services.exceptions.ConstraintException;
import com.sys.park.app.services.exceptions.DataIntegrityException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable("id") Integer id) {        
        CustomerDto customerDto = customerService.findById(id);
        return ResponseEntity.ok().body(customerDto);
    }
    
    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        List<CustomerDto> customerDtoList = customerService.findAll();
        return ResponseEntity.ok().body(customerDtoList);
    }

    @PostMapping
    public ResponseEntity<CustomerMensalDto> insert(@Valid @RequestBody CustomerForm customerForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        CustomerMensalDto customerDto = customerService.createNewCustomer(modelMapper.map(customerForm, CustomerMensalDto.class), 2);
        return ResponseEntity.ok().body(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(@Valid @RequestBody
        CustomerForm custumerForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        CustomerDto costumerDto = customerService.updateById(modelMapper.map(custumerForm, CustomerDto.class), id);
        return ResponseEntity.ok().body(costumerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<CustomerDto> finishCustomer(@Valid @RequestBody
        CustomerForm custumerForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        CustomerDto costumerDto = customerService.finishCustomer(id);
        return ResponseEntity.ok().body(costumerDto);
    }

    @GetMapping("/mensalistas")
    public ResponseEntity<Page<CustomerMensalDto>> findAllMov(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        try {
            Pageable pageable = Pageable.unpaged();

            if (page != null && size != null) {
                pageable = PageRequest.of(page, size);
            }

            Page<CustomerMensalDto> ticketDtoPage = customerService.findByCustomerTypePage(2, Optional.of(pageable));
            return ResponseEntity.ok().body(ticketDtoPage);     
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro de paginação");
        }
    }

    @PutMapping("/mensalistas/{id}")
    public ResponseEntity<CustomerMensalDto> updateMensal(@Valid @RequestBody
        CustomerForm custumerForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        CustomerMensalDto costumerDto = customerService.updateCustomer(modelMapper.map(custumerForm, CustomerMensalDto.class), id);
        return ResponseEntity.ok().body(costumerDto);
    }
}

