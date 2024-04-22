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

import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleForm;
import com.sys.park.app.models.VehicleModel;
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

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> find(@PathVariable("id") Integer id) {        
        VehicleDto vehicleDto = vehicleService.findById(id);
        return ResponseEntity.ok().body(vehicleDto);
    }

    @GetMapping("/mensalistas/{id}")
    public ResponseEntity<List<VehicleDto>> findVehiclesByIdCustomer(@PathVariable("id") Integer idCustomer) {
        List<VehicleDto> vehicleDtoList = vehicleService.findByCustomer(idCustomer);
        return ResponseEntity.ok().body(vehicleDtoList);
    }

    @PostMapping("/mensalistas")
    public ResponseEntity<VehicleDto> addMensalVehicle(@Valid @RequestBody VehicleForm vehicleForm, BindingResult br) {
    
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Restrição de Dados", errors);
        }

        VehicleDto vehicleDto = vehicleService.createVehicle(modelMapper.map(vehicleForm, VehicleDto.class), true);
        return ResponseEntity.ok().body(vehicleDto);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDto>> findAll() {
        List<VehicleDto> vehicleDtoList = vehicleService.findAll();
        return ResponseEntity.ok().body(vehicleDtoList);
    }

    @PostMapping
    public ResponseEntity<VehicleDto> insert(@Valid @RequestBody VehicleForm vehicleForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Restrição de Dados", errors);
        }

        VehicleModel vehicleModel = modelMapper.map(vehicleForm, VehicleModel.class);
        return ResponseEntity.ok().body(modelMapper.map(vehicleModel, VehicleDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> update(@Valid @RequestBody
        VehicleForm vehicleForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Restrição de Dados", errors);
        }
     
        VehicleDto vehicleDto = vehicleService.updateById(modelMapper.map(vehicleForm, VehicleDto.class), id);
        return ResponseEntity.ok().body(vehicleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
