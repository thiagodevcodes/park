package com.sys.park.app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Ticket.MovimentacaoDto;
import com.sys.park.app.dtos.Ticket.MovimentacaoForm;
import com.sys.park.app.dtos.Ticket.TicketDto;
import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleForm;
import com.sys.park.app.dtos.Ticket.TicketForm;
import com.sys.park.app.dtos.Vacancy.VacancyDto;
import com.sys.park.app.dtos.Vacancy.VacancyForm;
import com.sys.park.app.models.TicketModel;
import com.sys.park.app.repositories.TicketRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    CustomerService customerService;

    @Autowired
    VacancyService vacancyService;

    @Autowired
    PersonService personService;
   
    @Autowired
    private ModelMapper modelMapper;

    public TicketDto findById(Integer id) {
        try {
            TicketModel ticketModel = ticketRepository.findById(id).get();
            return modelMapper.map(ticketModel, TicketDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + TicketModel.class.getName());
        }
    }

    public Boolean verifyMovActiveByPlate(String plate) {
        try {
            VehicleDto vehicleDto = vehicleService.findByPlate(plate);

            if(vehicleDto != null) {
                Optional<TicketModel> ticketModel = ticketRepository.findByIdVehicleAndIsActive(vehicleDto.getId(), true);
            
                if(ticketModel.isPresent()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + plate + ", Tipo: " + TicketModel.class.getName());
        }
    }

    public List<MovimentacaoDto> findAllMov() {
        try {
            List<TicketDto> ticketDto = this.findAll();
            List<MovimentacaoDto> newDtoList = new ArrayList<>();       

            for (TicketDto ticket : ticketDto) {
                MovimentacaoDto newDto = new MovimentacaoDto();

                CustomerDto customerDto = customerService.findById(ticket.getIdCustomer());
                VehicleDto vehicleDto = vehicleService.findById(ticket.getIdVehicle());
                PersonDto personDto = personService.findById(customerDto.getIdPerson());

            
                if(personDto != null && vehicleDto != null && ticket.getExitTime() == null) {
                    newDto.setId(ticket.getId());
                    newDto.setEntryTime(ticket.getEntryTime());
                    newDto.setExitTime(ticket.getExitTime());
                    newDto.setName(personDto.getName());
                    newDto.setMake(vehicleDto.getMake());
                    newDto.setModel(vehicleDto.getModel());
                    newDto.setPlate(vehicleDto.getPlate());
                    newDto.setVacancy(ticket.getIdVacancy());
                    newDto.setIdCustomerType(customerDto.getIdCustomerType());
                    newDtoList.add(newDto);
                }
            } 
            
            return newDtoList;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível listar os Tickets!");
        }
    }

    public List<TicketDto> findAll() {
        try {
            List<TicketModel> ticketModelList = ticketRepository.findAll();

            return ticketModelList.stream()
                    .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar o Ticket!", e.getErrorMessages());
        }
    }

    public TicketDto insert(TicketDto ticketDto) {
        try {           
            TicketModel newTicket = modelMapper.map(ticketDto, TicketModel.class);

            newTicket = ticketRepository.save(newTicket);
            return modelMapper.map(newTicket, TicketDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Ticket não foi(foram) preenchido(s).");
        }
    }

    public TicketDto updateById(TicketForm ticketForm, Integer id) {
        try {
            Optional<TicketModel> ticketExist = ticketRepository.findById(id);

            if (ticketExist.isPresent()) {
                TicketModel ticketUpdated = ticketExist.get();
                
                modelMapper.map(ticketForm, ticketUpdated);   
                ticketUpdated.setId(id);
                ticketUpdated = ticketRepository.save(ticketUpdated);

                return modelMapper.map(ticketUpdated, TicketDto.class);
            }else{
                throw new DataIntegrityException("O Id do Ticket não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Ticket não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            if (ticketRepository.existsById(id)) {
                ticketRepository.deleteById(id);
            }else {
                throw new DataIntegrityException("O Id do Ticket não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir a Ticket!");
        }
    }

    public TicketDto createNewMov(MovimentacaoForm movimentacaoForm) {    
        try {
            VehicleDto vehicleDto = new VehicleDto();
            PersonDto personDto = new PersonDto();
            CustomerDto customerDto = new CustomerDto();
            VacancyDto vacancyDto = new VacancyDto();

            TicketDto ticketDto = new TicketDto();
            LocalDateTime time = LocalDateTime.now();

            if(movimentacaoForm.getVacancy() == null) {
                throw new DataIntegrityException("Selecione um vaga!");
            }

            vehicleDto = vehicleService.findByPlate(movimentacaoForm.getPlate());

            if(vehicleDto != null && movimentacaoForm.getIdCustomerType() == 1) {
                if(vehicleDto.getMonthlyVehicle() == true) {
                    throw new DataIntegrityException("Este veículo é mensalista");
                }
            } 

            if(this.verifyMovActiveByPlate(movimentacaoForm.getPlate())) {
                throw new DataIntegrityException("Movimentação já existe");
            }   

            vacancyDto = vacancyService.findById(movimentacaoForm.getVacancy());

            if(vacancyDto != null) {
                vacancyDto.setSituation(false);
                vacancyService.updateById(modelMapper.map(vacancyDto, VacancyForm.class), movimentacaoForm.getVacancy());
            }
       
            if(movimentacaoForm.getIdCustomerType() == 1) {
                personDto.setName(movimentacaoForm.getName());
                personDto = personService.insert(personDto, 1);
                
                customerDto.setIdCustomerType(1);
                customerDto.setIsActive(true);
                customerDto.setIdPerson(personDto.getId());
                customerDto = customerService.insert(customerDto);

                vehicleDto = vehicleService.findByPlate(movimentacaoForm.getPlate());

                if(vehicleDto == null) {
                    vehicleDto = new VehicleDto();

                    vehicleDto.setMake(movimentacaoForm.getMake());
                    vehicleDto.setModel(movimentacaoForm.getModel());
                    vehicleDto.setPlate(movimentacaoForm.getPlate());
                    vehicleDto.setIdCustomer(customerDto.getId());
                    vehicleDto.setMonthlyVehicle(false);

                    vehicleDto = vehicleService.insert(vehicleDto); 
                } else {
                    vehicleDto.setMonthlyVehicle(false);
                    vehicleDto = vehicleService.updateById(modelMapper.map(vehicleDto, VehicleForm.class), vehicleDto.getId());
                }
            } else if(movimentacaoForm.getIdCustomerType() == 2) {
                customerDto = customerService.findById(movimentacaoForm.getIdCustomer());
                vehicleDto = vehicleService.findById(movimentacaoForm.getIdVehicle());
                vehicleDto.setMonthlyVehicle(true);
                vehicleDto = vehicleService.updateById(modelMapper.map(vehicleDto, VehicleForm.class), vehicleDto.getId());
            }

            ticketDto.setEntryTime(time);
            ticketDto.setExitTime(null);
            ticketDto.setIdCustomer(customerDto.getId());
            ticketDto.setIdVacancy(vacancyDto.getId());
            ticketDto.setIdVehicle(vehicleDto.getId());
            ticketDto.setIsActive(true);

            ticketDto = this.insert(ticketDto);

            return ticketDto;    
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível criar o Ticket!", e);
        }
    }

    public TicketDto finishMovimentacao(TicketForm ticketForm, Integer id) {
        try {
            Optional<TicketModel> ticketExist = ticketRepository.findById(id);
            LocalDateTime date = LocalDateTime.now();
            

            if (ticketExist.isPresent()) {
                ticketForm.setExitTime(date);
                ticketForm.setIsActive(false);
                TicketDto ticketDto = this.updateById(ticketForm, id);

                VacancyDto vacancyDto = vacancyService.findById(ticketDto.getIdVacancy());
                vacancyDto.setSituation(true);
                vacancyDto = vacancyService.updateById(modelMapper.map(vacancyDto, VacancyForm.class), ticketDto.getIdVacancy());
                VehicleDto vehicleDto = vehicleService.findById(ticketDto.getIdVehicle());
        
                vehicleDto = vehicleService.updateById(modelMapper.map(vehicleDto, VehicleForm.class), ticketDto.getIdVehicle());
                
                return ticketDto;
            }else {
                throw new DataIntegrityException("O Id do Ticket não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).", e);
        }
    }
}
