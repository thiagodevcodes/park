package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
    VehicleRepository vehicleRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    public VehicleDto findById(Integer id) {
        try {
            VehicleModel vehicleModel = vehicleRepository.findById(id).get();
            return modelMapper.map(vehicleModel, VehicleDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + VehicleModel.class.getName());
        }
    }

    public List<VehicleDto> findAll() {
        try {
            List<VehicleModel> vehicleModelList = vehicleRepository.findAll();

            return vehicleModelList.stream()
                    .map(vehicle -> modelMapper.map(vehicle, VehicleDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar o Veiculo!", e.getErrorMessages());
        }
    }

    public VehicleDto insert(VehicleDto vehicleDto) {
        try {
            VehicleModel newVehicle = modelMapper.map(vehicleDto, VehicleModel.class);
            
            Optional<VehicleModel> byPlate = vehicleRepository.findByPlate(newVehicle.getPlate());
            
            if (byPlate.isPresent()) {
                throw new DataIntegrityException("Veiculo já registrado.");
            }

            newVehicle = vehicleRepository.save(newVehicle);
            return modelMapper.map(newVehicle, VehicleDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Veiculo não foi(foram) preenchido(s).");
        }
    }

    public VehicleDto updateById(VehicleDto vehicleDto, Integer id) {
        try {
            Optional<VehicleModel> byPlate = vehicleRepository.findByPlate(vehicleDto.getPlate());

            if (byPlate.isPresent()) {
                VehicleModel vehicleUpdated = byPlate.get();

                modelMapper.map(vehicleDto, vehicleUpdated);
                vehicleUpdated.setId(id);
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
            if (vehicleRepository.existsById(id)) {
                vehicleRepository.deleteById(id);
            }else {
                throw new DataIntegrityException("O Id do Veiculo não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir a Pessoa!");
        }
    }

    public List<VehicleDto> findByCustomer(Integer idCustomer) {
        try {
            List<VehicleModel> vehicles = vehicleRepository.findByIdCustomerAndMonthlyVehicle(idCustomer, true);
            
            return vehicles.stream()
            .map(vehicle -> modelMapper.map(vehicle, VehicleDto.class))
            .collect(Collectors.toList());

        } catch (NoSuchElementException e) {
            throw new DataIntegrityException("O Id do Veiculo não existe na base de dados!");
        }
    }

    public VehicleDto findByPlate(String plate) {
        try {
            Optional<VehicleModel> vehicleModel = vehicleRepository.findByPlate(plate);

            if(vehicleModel.isPresent()) {
                return modelMapper.map(vehicleModel.get(), VehicleDto.class);
            } else {
                return null;
            }
        } catch (NoSuchElementException e) {
            throw new DataIntegrityException("O veículo não existe na base de dados!");
        }
    }

    public VehicleDto createVehicle(VehicleDto vehicleDto, Boolean monthlyVehicle) {
        try {
            VehicleDto vehicle = new VehicleDto();

            Optional<VehicleModel> plateExist = vehicleRepository.findByPlate(vehicleDto.getPlate());

            if(plateExist.isPresent()) {
                VehicleModel vehicleModel = plateExist.get();

                vehicle.setIdCustomer(vehicleDto.getIdCustomer());
                vehicle.setMonthlyVehicle(monthlyVehicle);
                
                vehicle = this.updateById(vehicleDto, vehicleModel.getId());
                
            } else {

                vehicle.setPlate(vehicleDto.getPlate());
                vehicle.setMake(vehicleDto.getMake());
                vehicle.setModel(vehicleDto.getModel());
                vehicle.setMonthlyVehicle(monthlyVehicle);
                vehicle.setIdCustomer(vehicleDto.getIdCustomer());

                vehicle = this.insert(vehicle);
            }
            
            return vehicle;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro ao criar o veículo!");
        }   
    }

    public VehicleDto finishVehicle(VehicleForm vehicleForm, Integer id) {
        try {
            Optional<VehicleModel> vehicleExist = vehicleRepository.findById(id);

            if (vehicleExist.isPresent()) {
                VehicleModel vehicleModel = vehicleExist.get();
                vehicleModel.setMonthlyVehicle(false);
                
                VehicleDto vehicleDto = this.updateById(modelMapper.map(vehicleModel, VehicleDto.class), id);

                return vehicleDto;
            }else {
                throw new DataIntegrityException("O Id do Veiculo não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).", e);
        }
    }

    public Boolean verifyPlate(String plate) {
        try {
            Optional<VehicleModel> plateExist = vehicleRepository.findByPlate(plate);


            if(plateExist.isPresent()) {
                return true;
            } else {
                return false;
            }

        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + plate + ", Tipo: " + VehicleModel.class.getName());
        }
    }

}
