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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Ticket.MovimentacaoDto;
import com.sys.park.app.dtos.Ticket.MovimentacaoForm;
import com.sys.park.app.dtos.Ticket.TicketDto;
import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vacancy.VacancyDto;
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

    public Page<MovimentacaoDto> findAllMov(Optional<Pageable> optionalPage) {
        try {
            Pageable page = optionalPage.orElse(Pageable.unpaged());
            Page<TicketModel> pageableModel = ticketRepository.findByIsActive(true, page);
            List<MovimentacaoDto> newDtoList = new ArrayList<>();           

            for (TicketModel ticket : pageableModel) {
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
                    newDto.setIdVacancy(ticket.getIdVacancy());
                    newDto.setIdCustomerType(customerDto.getIdCustomerType());
                    newDto.setIdVehicle(vehicleDto.getId());
                    newDto.setIdCustomer(customerDto.getId());
                    newDtoList.add(newDto);
                }
            } 

            Page<MovimentacaoDto> pageableMov = new PageImpl<>(newDtoList, page, pageableModel.getTotalElements());
            return pageableMov;
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

    public TicketDto updateById(TicketDto ticketDto, Integer id) {
        try {
            Optional<TicketModel> ticketExist = ticketRepository.findById(id);

            if (ticketExist.isPresent()) {
                TicketModel ticketUpdated = ticketExist.get();
                
                modelMapper.map(ticketDto, ticketUpdated);   
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
                TicketDto ticketDto = this.findById(id);
                VacancyDto vacancyDto = vacancyService.findById(ticketDto.getIdVacancy());
                vacancyDto.setSituation(true);
                vacancyService.updateById(vacancyDto, vacancyDto.getId());
                ticketRepository.deleteById(id);
            }else {
                throw new DataIntegrityException("O Id do Ticket não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir a Ticket!");
        }
    }

    public TicketDto createNewMov(MovimentacaoDto movimentacaoDto) {
        try {
            if (movimentacaoDto.getIdVacancy() == null) {
                throw new DataIntegrityException("Selecione uma vaga!");
            }
    
            VehicleDto vehicleDto = vehicleService.findByPlate(movimentacaoDto.getPlate());
    
            if (vehicleDto != null && movimentacaoDto.getIdCustomerType() == 1 && vehicleDto.getMonthlyVehicle()) {
                throw new DataIntegrityException("Este veículo é mensalista");
            }
    
            if (movimentacaoDto.getIdCustomerType() == 1 && this.verifyMovActiveByPlate(movimentacaoDto.getPlate())) {
                throw new DataIntegrityException("Movimentação já existe");
            }
    
            PersonDto personDto = new PersonDto();
            CustomerDto customerDto = new CustomerDto();

            if (movimentacaoDto.getIdCustomerType() == 1) {
                personDto.setName(movimentacaoDto.getName());
                personDto = personService.insert(personDto, 1);
    
                customerDto.setIdCustomerType(1);
                customerDto.setIsActive(true);
                customerDto.setIdPerson(personDto.getId());
                customerDto = customerService.insert(customerDto);
    
                if (vehicleDto == null) {
                    vehicleDto = new VehicleDto();
                    vehicleDto.setMake(movimentacaoDto.getMake());
                    vehicleDto.setModel(movimentacaoDto.getModel());
                    vehicleDto.setPlate(movimentacaoDto.getPlate());
                    vehicleDto.setIdCustomer(customerDto.getId());
                    vehicleDto.setMonthlyVehicle(false);
                    vehicleDto = vehicleService.insert(vehicleDto);
                } else {
                    vehicleDto.setMonthlyVehicle(false);
                    vehicleDto = vehicleService.updateById(vehicleDto, vehicleDto.getId());
                }
            } else if (movimentacaoDto.getIdCustomerType() == 2) {
                customerDto = customerService.findById(movimentacaoDto.getIdCustomer());
                vehicleDto = vehicleService.findById(movimentacaoDto.getIdVehicle());

                if(this.verifyMovActiveByPlate(vehicleDto.getPlate())) {
                    throw new DataIntegrityException("Movimentação já existe");
                }

                vehicleDto.setMonthlyVehicle(true);
                vehicleDto = vehicleService.updateById(vehicleDto, vehicleDto.getId());
            }

            VacancyDto vacancyDto = vacancyService.findById(movimentacaoDto.getIdVacancy());
            if (vacancyDto != null) {
                vacancyDto.setSituation(false);
                vacancyService.updateById(vacancyDto, movimentacaoDto.getIdVacancy());
            }
            
            TicketDto ticketDto = new TicketDto();
            
            ticketDto.setEntryTime(LocalDateTime.now());
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

    public TicketDto finishMovimentacao(TicketDto ticketDto, Integer id) {
        try {
            Optional<TicketModel> ticketExist = ticketRepository.findById(id);
            LocalDateTime date = LocalDateTime.now();
            
            if (ticketExist.isPresent()) {
                ticketDto.setExitTime(date);
                ticketDto.setIsActive(false);
                ticketDto = this.updateById(ticketDto, id);
                
                VacancyDto vacancyDto = vacancyService.findById(ticketDto.getIdVacancy());
                vacancyDto.setSituation(true);
                vacancyDto = vacancyService.updateById(vacancyDto, ticketDto.getIdVacancy());
                VehicleDto vehicleDto = vehicleService.findById(ticketDto.getIdVehicle());
        
                vehicleDto = vehicleService.updateById(vehicleDto, ticketDto.getIdVehicle());
                
                return ticketDto;
            }else {
                throw new DataIntegrityException("O Id do Ticket não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).", e);
        }
    }

    public TicketDto updateMovimentacao(MovimentacaoForm movimentacaoForm, Integer id) {
        TicketDto ticketDto = this.findById(id);
        VehicleDto vehicleDto = vehicleService.findById(ticketDto.getIdVehicle());
        CustomerDto customerDto = customerService.findById(ticketDto.getIdCustomer());
        PersonDto personDto = personService.findById(customerDto.getIdPerson());
        VacancyDto vacancyDto = vacancyService.findById(ticketDto.getIdVacancy());

        if(movimentacaoForm.getIdCustomerType() == 1) {
            personDto.setName(movimentacaoForm.getName());
            personService.updateById(personDto, personDto.getId());

            vehicleDto.setMake(movimentacaoForm.getMake());
            vehicleDto.setModel(movimentacaoForm.getModel());
            vehicleDto.setPlate(movimentacaoForm.getPlate());

            if(vehicleService.verifyPlate(movimentacaoForm.getPlate())) {
                vehicleDto = vehicleService.findByPlate(movimentacaoForm.getPlate());
                vehicleService.updateById(vehicleDto, vehicleDto.getId()); 
            } else {
                vehicleDto = vehicleService.createVehicle(vehicleDto, false);   
            }

        } else if(movimentacaoForm.getIdCustomerType() == 2) {
            vehicleDto = vehicleService.findById(movimentacaoForm.getIdVehicle());
            customerDto = customerService.findById(movimentacaoForm.getIdCustomer());
            ticketDto.setIdCustomer(customerDto.getId());
        }

        if(movimentacaoForm.getIdVacancy() != null) {
            vacancyDto.setSituation(true);
            vacancyService.updateById(vacancyDto, ticketDto.getIdVacancy());

            VacancyDto newVacancy = vacancyService.findById(movimentacaoForm.getIdVacancy());
            newVacancy.setSituation(false);
            vacancyService.updateById(newVacancy, movimentacaoForm.getIdVacancy());
        }

        ticketDto.setIdVacancy(movimentacaoForm.getIdVacancy());
        ticketDto.setIdVehicle(vehicleDto.getId());
        this.updateById(ticketDto, ticketDto.getId());
        
        return ticketDto;
    }
}
