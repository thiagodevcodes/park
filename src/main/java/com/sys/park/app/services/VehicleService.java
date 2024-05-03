package com.sys.park.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.CustomerVehicle.CustomerVehicleDto;
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
    private CustomerVehicleService customerVehicleService;
    
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

    public VehicleDto insert(VehicleDto vehicleDto) {
        try {
            VehicleModel newVehicle = modelMapper.map(vehicleDto, VehicleModel.class);
            
            validVehicle(vehicleDto.getPlate());

            newVehicle = vehicleRepository.save(newVehicle);
            return modelMapper.map(newVehicle, VehicleDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Veiculo não foi(foram) preenchido(s).");
        }
    }

    public VehicleDto createVehicle(VehicleDto vehicleDto, Boolean monthlyVehicle, Integer idCustomer) {
        try {
            VehicleDto vehicle = new VehicleDto();

            Optional<VehicleModel> plateExist = vehicleRepository.findByPlate(vehicleDto.getPlate());

            if(plateExist.isPresent()) {
                VehicleModel vehicleModel = plateExist.get();

                if(vehicleModel.getMonthlyVehicle().equals(true)) {
                    throw new BusinessRuleException("Veículo pertence a um mensalista!");
                }
                
                vehicle.setMonthlyVehicle(monthlyVehicle);
                
                vehicle = this.updateById(vehicleDto, vehicleModel.getId());
                
            } else {

                vehicle.setPlate(vehicleDto.getPlate());
                vehicle.setMake(vehicleDto.getMake());
                vehicle.setModel(vehicleDto.getModel());
                vehicle.setMonthlyVehicle(monthlyVehicle);

                vehicle = this.insert(vehicle);
            }
            
            CustomerVehicleDto customerVehicleDto = new CustomerVehicleDto();
            customerVehicleDto.setIdCustomer(idCustomer);
            customerVehicleDto.setIdVehicle(vehicle.getId());
            customerVehicleDto = customerVehicleService.insert(customerVehicleDto);

            return vehicle;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro ao criar o veículo!");
        }   
    }

    public VehicleDto updateById(VehicleDto vehicleDto, Integer id) {
        try {
            Optional<VehicleModel> byPlate = vehicleRepository.findById(id);

            if(!byPlate.get().getPlate().equals(vehicleDto.getPlate())) {
                validVehicle(vehicleDto.getPlate());
            }

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
            throw new DataIntegrityException("Erro ao finalizar o veículo!", e);
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

    public Page<VehicleDto> getMensalByIdCustomer(Integer idCustomer, Optional<Pageable> optionalPage) {
        try {
            Pageable page = optionalPage.orElse(Pageable.unpaged());
            List<CustomerVehicleDto> customerVehicleModels = customerVehicleService.findByIdCustomer(idCustomer);
            List<VehicleDto> vehicleDtos = new ArrayList<>();

            for (CustomerVehicleDto customerVehicleDto : customerVehicleModels) {
                VehicleDto vehicleDto = this.findById(customerVehicleDto.getIdVehicle());

                if(vehicleDto.getMonthlyVehicle()) {
                    vehicleDtos.add(vehicleDto);
                }
            }

            Page<VehicleDto> pageVehicleDto =  new PageImpl<>(vehicleDtos, page, vehicleDtos.size());
            return pageVehicleDto;
        } catch (NoSuchElementException e) {
            throw new DataIntegrityException("O Id do Veiculo não existe na base de dados!");
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

        } catch (BusinessRuleException e) {
            throw new NotFoundException("Erro ao verificar veiculo!");
        }
    }

    public Boolean validVehicle(String plate) {
        try {
            Optional<VehicleModel> plateExist = vehicleRepository.findByPlate(plate);

            if(plateExist.isPresent()) {
                throw new DataIntegrityException("Veículo já cadastrado!");
            }

            return false;

        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Placa: " + plate + ", Tipo: " + VehicleModel.class.getName());
        }
    }
}
