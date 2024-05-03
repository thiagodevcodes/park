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

import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleForm;
import com.sys.park.app.services.VehicleService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<VehicleDto>> findAll() {
        List<VehicleDto> vehicleDtoList = vehicleService.findAll();
        return ResponseEntity.ok().body(vehicleDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> findById(@PathVariable("id") Integer id) {        
        VehicleDto vehicleDto = vehicleService.findById(id);
        return ResponseEntity.ok().body(vehicleDto);
    }

    @GetMapping("/mensalistas/{id}")
    public ResponseEntity<Page<VehicleDto>> findByIdCustomer(@PathVariable("id") Integer idCustomer, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        Pageable pageable = Pageable.unpaged();

        if (page != null && size != null) {
            pageable = PageRequest.of(page, size);
        }

        Page<VehicleDto> vehicleDtoList = vehicleService.getMensalByIdCustomer(idCustomer, Optional.of(pageable));
        return ResponseEntity.ok().body(vehicleDtoList);
    }

    @PostMapping
    public ResponseEntity<VehicleDto> insert(@Valid @RequestBody VehicleForm vehicleForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Dados incorretos!", errors);
        }

        VehicleDto vehicleDto = vehicleService.createVehicle(modelMapper.map(vehicleForm, VehicleDto.class), vehicleForm.getMonthlyVehicle(), vehicleForm.getIdCustomer());
        return ResponseEntity.ok().body(vehicleDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> updateById(@Valid @RequestBody
        VehicleForm vehicleForm, @PathVariable("id") Integer id, BindingResult br) {
       
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
