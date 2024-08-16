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
import com.sys.park.app.dtos.Customer.CustomerRequest;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.models.CustomerModel;
import com.sys.park.app.models.PersonModel;
import com.sys.park.app.services.CustomerService;
import com.sys.park.app.services.PersonService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    PersonService personService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        List<CustomerDto> customerDtoList = customerService.findAll();
        return ResponseEntity.ok().body(customerDtoList);
    }

    @GetMapping("find")
    public ResponseEntity<CustomerDto> findById(@RequestParam("id") Long id) {        
        CustomerDto customerDto = customerService.findById(id);
        return ResponseEntity.ok().body(customerDto);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> insert(@Valid @RequestBody CustomerRequest customerRequest, BindingResult br) {       
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        CustomerModel customerModel = modelMapper.map(customerRequest, CustomerModel.class);
        PersonModel personModel = modelMapper.map(customerRequest.getPerson(), PersonModel.class);
        
        PersonDto person = personService.insert(personModel);
        customerModel.setIdPerson(person.getId());

        CustomerDto customer = customerService.insert(customerModel);
        customer.setPerson(person);

        //CustomerDto customerDto = customerService.addCustomer(customerRequest);
        return ResponseEntity.ok().body(customer);
    }

    @PutMapping
    public ResponseEntity<CustomerDto> updateById(@Valid @RequestBody
        CustomerRequest customerRequest, @RequestParam("id") Long id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        CustomerDto costumerDto = customerService.updateCustomer(customerRequest, id);
        return ResponseEntity.ok().body(costumerDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(@RequestParam("id") Long id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

