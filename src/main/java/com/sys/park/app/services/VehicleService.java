package com.sys.park.app.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.CustomerVehicle.CustomerVehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleRequest;
import com.sys.park.app.models.CustomerModel;
import com.sys.park.app.models.VehicleModel;
import com.sys.park.app.repositories.VehicleRepository;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

   
    @Autowired
    private ModelMapper modelMapper;

    public Page<VehicleDto> findAll(Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(vehicle -> {
            VehicleDto vehicleDto = modelMapper.map(vehicle, VehicleDto.class);

            return vehicleDto;
        });
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

    public VehicleDto insert(VehicleModel vehicleModel) {
        try {
            if(vehicleRepository.existsByPlate(vehicleModel.getPlate())) {
                vehicleModel = vehicleRepository.findByPlate(vehicleModel.getPlate()).get();
            } else {
                vehicleModel = vehicleRepository.save(vehicleModel);
            }
        
            return modelMapper.map(vehicleModel, VehicleDto.class);

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
            System.out.println("Id: " + id);
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
