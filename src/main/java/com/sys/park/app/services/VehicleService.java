package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleForm;
import com.sys.park.app.models.VehicleModel;
import com.sys.park.app.repositories.VehicleRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
   
    @Autowired
    private ModelMapper modelMapper;

    public List<VehicleDto> findAll() {
        try {
            List<VehicleModel> vehicleModelList = vehicleRepository.findAll();

            return vehicleModelList.stream()
                    .map(vehicle -> modelMapper.map(vehicle, VehicleDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar o Veiculo!");
        }
    }

    public VehicleDto findById(Integer id) {
        try {
            VehicleModel vehicleModel = vehicleRepository.findById(id).get();
            return modelMapper.map(vehicleModel, VehicleDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + VehicleModel.class.getName());
        }
    }

    public VehicleDto findByPlate(String plate) {
        try {
            VehicleModel vehicleModel = vehicleRepository.findByPlate(plate).get();
            return modelMapper.map(vehicleModel, VehicleDto.class);
            
        } catch (NoSuchElementException e) {
            throw new NotFoundException("O veículo não existe na base de dados!");
        }
    }

    public VehicleDto insert(VehicleForm vehicleForm) {
        try {
            VehicleModel newVehicle = modelMapper.map(vehicleForm, VehicleModel.class);

            if(vehicleRepository.existsByPlate(vehicleForm.getPlate())) {
                newVehicle = vehicleRepository.findByPlate(vehicleForm.getPlate()).get();
            } else {
                newVehicle = vehicleRepository.save(newVehicle);
            }
        
            return modelMapper.map(newVehicle, VehicleDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Veiculo não foi(foram) preenchido(s).");
        }
    }

    public VehicleDto updateById(VehicleDto vehicleDto, Integer id) {
        try {           
            Optional<VehicleModel> byPlate = vehicleRepository.findById(id);

            if (byPlate.isPresent()) {
                VehicleModel vehicleUpdated = byPlate.get();

                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(vehicleDto, vehicleUpdated);
                
                vehicleUpdated = vehicleRepository.save(vehicleUpdated);

                return modelMapper.map(vehicleUpdated, VehicleDto.class);
            }else{
                throw new DataIntegrityException("O Id do Veiculo não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Veiculo não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            System.out.println(id);
            if (vehicleRepository.existsById(id)) {
                vehicleRepository.deleteById(id);
            }else {
                throw new DataIntegrityException("O Id do Veiculo não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Veiculo!");
        }
    }
}
