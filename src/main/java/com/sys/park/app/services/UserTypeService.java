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

import com.sys.park.app.dtos.UserType.UserTypeDto;
import com.sys.park.app.dtos.UserType.UserTypeForm;
import com.sys.park.app.models.UserTypeModel;
import com.sys.park.app.repositories.UserTypeRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class UserTypeService {
    @Autowired
    private UserTypeRepository userTypeRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    public List<UserTypeDto> findAll() {
        try {
            List<UserTypeModel> customerTypeModelList = userTypeRepository.findAll();

            return customerTypeModelList.stream()
                    .map(customerType -> modelMapper.map(customerType, UserTypeDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar o Tipo do Cliente!");
        }
    }

    public UserTypeDto findById(Integer id) {
        try {
            UserTypeModel customerTypeModel = userTypeRepository.findById(id).get();
            return modelMapper.map(customerTypeModel, UserTypeDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + UserTypeDto.class.getName());
        }
    }

    public UserTypeDto insert(UserTypeDto userTypeDto) {
        try {
            UserTypeModel newUserType = modelMapper.map(userTypeDto, UserTypeModel.class);
            
            Optional<UserTypeModel> byName = userTypeRepository.findByName(newUserType.getName());
        
            if (byName.isPresent()) {
                throw new DataIntegrityException("Tipo de Cliente já registrado.");
            }
            
            newUserType = userTypeRepository.save(newUserType);
            return modelMapper.map(newUserType, UserTypeDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Tipo do Cliente não foi(foram) preenchido(s).");
        }
    }

    public UserTypeDto updateById(UserTypeForm userTypeForm, Integer id) {
        try {
            Optional<UserTypeModel> userTypeExist = userTypeRepository.findById(id);

            if (userTypeExist.isPresent()) {
                UserTypeModel userTypeUpdated = userTypeExist.get();

                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(userTypeForm, userTypeUpdated);
                
                userTypeUpdated = userTypeRepository.save(userTypeUpdated);

                return modelMapper.map(userTypeUpdated, UserTypeDto.class);
            }else{
                throw new DataIntegrityException("O Id do Tipo do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Tipo do Cliente não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            if (userTypeRepository.existsById(id)) {
                userTypeRepository.deleteById(id);

            }else {
                throw new DataIntegrityException("O Id do Tipo do Cliente não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Cliente!");
        }
    }
}


