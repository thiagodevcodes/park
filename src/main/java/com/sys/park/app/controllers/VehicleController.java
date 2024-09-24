package com.sys.park.app.controllers;

import java.util.ArrayList;
import java.util.List;
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

import com.sys.park.app.dtos.CustomerVehicle.CustomerVehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleRequest;
import com.sys.park.app.services.CustomerVehicleService;
import com.sys.park.app.services.VehicleService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @Autowired 
    CustomerVehicleService customerVehicleService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Page<VehicleDto>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<VehicleDto> vehiclePage = vehicleService.findAll(pageable);
        return ResponseEntity.ok(vehiclePage);
    }


    @GetMapping("/customer")
    public ResponseEntity<Page<CustomerVehicleDto>> listVehiclesByIdCustomer(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam("id") Long id) {
        
        Pageable pageable = PageRequest.of(page, size);

        Page<CustomerVehicleDto> vehiclePage = customerVehicleService.findByIdCustomer(pageable, id);
        return ResponseEntity.ok(vehiclePage);
    }

    @GetMapping("find")
    public ResponseEntity<VehicleDto> findById(@RequestParam("id") Integer id) {        
        VehicleDto vehicleDto = vehicleService.findById(id);
        return ResponseEntity.ok().body(vehicleDto);
    }

    @PostMapping
    public ResponseEntity<VehicleDto> insert(@Valid @RequestBody VehicleRequest vehicleForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        VehicleDto vehicleDto = modelMapper.map(vehicleForm, VehicleDto.class);

        vehicleDto = customerVehicleService.addVehicle(vehicleForm.getIdCustomer(), vehicleDto);

        return ResponseEntity.ok().body(vehicleDto);
    }

    @PutMapping
    public ResponseEntity<VehicleDto> updateById(@Valid @RequestBody
        VehicleRequest vehicleForm, @RequestParam("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Dados incorretos!", errors);
        }
     
        VehicleDto vehicleDto = vehicleService.updateById(modelMapper.map(vehicleForm, VehicleDto.class), id);
        return ResponseEntity.ok().body(vehicleDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(@RequestParam("id") Integer id) {
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
