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

import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleRequest;
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

        VehicleDto vehicleDto = vehicleService.insert(vehicleForm);
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
