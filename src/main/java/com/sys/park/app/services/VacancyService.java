package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Vacancy.VacancyDto;
import com.sys.park.app.dtos.Vacancy.VacancyForm;
import com.sys.park.app.models.PersonModel;
import com.sys.park.app.models.VacancyModel;
import com.sys.park.app.repositories.VacancyRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class VacancyService {
    @Autowired
    VacancyRepository vacancyRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    public VacancyDto findById(Integer id) {
        try {
            VacancyModel vacancyModel = vacancyRepository.findById(id).get();
            return modelMapper.map(vacancyModel, VacancyDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + PersonModel.class.getName());
        }
    }

    public List<VacancyDto> findAll() {
        try {
            List<VacancyModel> vacancyModelList = vacancyRepository.findAll();

            return vacancyModelList.stream()
                    .map(user -> modelMapper.map(user, VacancyDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar a Vaga!", e.getErrorMessages());
        }
    }

    public VacancyDto insert(VacancyForm vacancyForm) {
        try {
            VacancyModel newVacancy = modelMapper.map(vacancyForm, VacancyModel.class);
            
            newVacancy = vacancyRepository.save(newVacancy);
            return modelMapper.map(newVacancy, VacancyDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Vaga não foi(foram) preenchido(s).");
        }
    }

    public VacancyDto updateById(VacancyDto vacancyDto, Integer id) {
        try {
            Optional<VacancyModel> vacancyExist = vacancyRepository.findById(id);

            if (vacancyExist.isPresent()) {
                VacancyModel vacancyUpdated = vacancyExist.get();

                modelMapper.map(vacancyDto, vacancyUpdated);
                vacancyUpdated = vacancyRepository.save(vacancyUpdated);

                return modelMapper.map(vacancyUpdated, VacancyDto.class);
            }else{
                throw new DataIntegrityException("O Id da Vaga não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Vaga não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            if (vacancyRepository.existsById(id)) {
                vacancyRepository.deleteById(id);

            }else {
                throw new DataIntegrityException("O Id da Vaga não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir a Vaga!");
        }
    }

    public Integer vacanciesOcuppied() {
        return vacancyRepository.countBySituationFalse();
    }

    public Integer vacanciesNotOcuppuied() {
        return vacancyRepository.countBySituationTrue();
    }
}
