package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.CustomerVehicle.CustomerVehicleDto;

import com.sys.park.app.models.CustomerVehicleModel;
import com.sys.park.app.repositories.CustomerVehicleRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class CustomerVehicleService {
    @Autowired
    CustomerVehicleRepository customerVehicleRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CustomerVehicleDto findById(Integer id) {
        try {
            CustomerVehicleModel customerVehicleModel = customerVehicleRepository.findById(id).get();
            return modelMapper.map(customerVehicleModel, CustomerVehicleDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }

    public List<CustomerVehicleDto> findByIdCustomer(Integer idCustomer) {
        try {
            List<CustomerVehicleModel> customerVehicleModel = customerVehicleRepository.findByIdCustomer(idCustomer);
            
            return customerVehicleModel.stream()
            .map(customerVehicle -> modelMapper.map(customerVehicle, CustomerVehicleDto.class))
            .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + idCustomer + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }   

    public CustomerVehicleDto findByIdCustomerAndIdVehicle(Integer idCustomer, Integer idVehicle) {
        try {
            CustomerVehicleModel customerVehicleModel = customerVehicleRepository.findByIdCustomerAndIdVehicle(idCustomer, idVehicle).get();
            return modelMapper.map(customerVehicleModel, CustomerVehicleDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + idCustomer + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }   

    public Boolean vehicleCustomerExist(Integer idCustomer, Integer idVehicle) {
        try {
            Optional<CustomerVehicleModel> customerVehicle = customerVehicleRepository.findByIdCustomerAndIdVehicle(idCustomer, idVehicle);

            if(customerVehicle.isPresent()) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + idCustomer + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }   


    
    public List<CustomerVehicleDto> findByIdVehicle(Integer idCustomer) {
        try {
            List<CustomerVehicleModel> customerVehicleModel = customerVehicleRepository.findByIdCustomer(idCustomer);
            
            return customerVehicleModel.stream()
            .map(customerVehicle -> modelMapper.map(customerVehicle, CustomerVehicleDto.class))
            .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + idCustomer + ", Tipo: " + CustomerVehicleModel.class.getName());
        }
    }  

    public List<CustomerVehicleDto> findAll() {
        try {
            List<CustomerVehicleModel> customerTypeModelList = customerVehicleRepository.findAll();

            return customerTypeModelList.stream()
                    .map(customerVehicle -> modelMapper.map(customerVehicle, CustomerVehicleDto.class))
                    .collect(Collectors.toList());
            } catch (BusinessRuleException e) {
                throw new BusinessRuleException("Não é possível consultar o Tipo do Cliente!");
            }
        }

        public CustomerVehicleDto insert(CustomerVehicleDto customerVehicleDto) {
            try {
                CustomerVehicleModel newCustomer = modelMapper.map(customerVehicleDto, CustomerVehicleModel.class);
                
                newCustomer = customerVehicleRepository.save(newCustomer);
                return modelMapper.map(newCustomer, CustomerVehicleDto.class);
    
            } catch (DataIntegrityViolationException e) {
                throw new DataIntegrityException("Erro ao tentar inserir um cliente!");
        }
        
    }
}
