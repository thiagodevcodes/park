package com.sys.park.app.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.CustomerVehicle.CustomerVehicleDto;
import com.sys.park.app.dtos.CustomerVehicle.CustomerVehicleForm;
import com.sys.park.app.services.CustomerVehicleService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicle_customer")
public class CustomerVehicleController {
    @Autowired
    CustomerVehicleService customerVehicleService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CustomerVehicleDto>> findAll() {
        List<CustomerVehicleDto> customerVehicleDto = customerVehicleService.findAll();
        return ResponseEntity.ok().body(customerVehicleDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerVehicleDto> findById(@PathVariable("id") Integer id) {        
        CustomerVehicleDto customerVehicleDto = customerVehicleService.findById(id);
        return ResponseEntity.ok().body(customerVehicleDto);
    }
    
    @PostMapping
    public ResponseEntity<CustomerVehicleDto> insert(@Valid @RequestBody CustomerVehicleForm customerVehicleForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        CustomerVehicleDto customerVehicleDto = customerVehicleService.insert(modelMapper.map(customerVehicleForm, CustomerVehicleDto.class));
        return ResponseEntity.ok().body(customerVehicleDto);
    }
}
