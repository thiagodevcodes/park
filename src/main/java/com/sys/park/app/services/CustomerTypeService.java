package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.CustomerType.CustomerTypeDto;
import com.sys.park.app.dtos.CustomerType.CustomerTypeForm;
import com.sys.park.app.models.CustomerTypeModel;
import com.sys.park.app.repositories.CustomerTypeRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class CustomerTypeService {
    @Autowired
    private CustomerTypeRepository customerTypeRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    public List<CustomerTypeDto> findAll() {
        try {
            List<CustomerTypeModel> customerTypeModelList = customerTypeRepository.findAll();

            return customerTypeModelList.stream()
                    .map(customerType -> modelMapper.map(customerType, CustomerTypeDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar o Tipo do Cliente!");
        }
    }

    public CustomerTypeDto findById(Integer id) {
        try {
            CustomerTypeModel customerTypeModel = customerTypeRepository.findById(id).get();
            return modelMapper.map(customerTypeModel, CustomerTypeDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + CustomerTypeDto.class.getName());
        }
    }

    public CustomerTypeDto insert(CustomerTypeDto customerTypeDto) {
        try {
            CustomerTypeModel newCustomerType = modelMapper.map(customerTypeDto, CustomerTypeModel.class);
            
            Optional<CustomerTypeModel> byName = customerTypeRepository.findByName(newCustomerType.getName());
        
            if (byName.isPresent()) {
                throw new DataIntegrityException("Tipo de Cliente já registrado.");
            }
            
            newCustomerType = customerTypeRepository.save(newCustomerType);
            return modelMapper.map(newCustomerType, CustomerTypeDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Tipo do Cliente não foi(foram) preenchido(s).");
        }
    }

    public CustomerTypeDto updateById(CustomerTypeForm customerTypeForm, Integer id) {
        try {
            Optional<CustomerTypeModel> customerTypeExist = customerTypeRepository.findById(id);

            if (customerTypeExist.isPresent()) {
                CustomerTypeModel customerTypeUpdated = customerTypeExist.get();

                modelMapper.map(customerTypeForm, customerTypeUpdated);
                customerTypeUpdated = customerTypeRepository.save(customerTypeUpdated);

                return modelMapper.map(customerTypeUpdated, CustomerTypeDto.class);
            }else{
                throw new DataIntegrityException("O Id do Tipo do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Tipo do Cliente não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            if (customerTypeRepository.existsById(id)) {
                customerTypeRepository.deleteById(id);

            }else {
                throw new DataIntegrityException("O Id do Tipo do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Cliente!");
        }
    }
}
