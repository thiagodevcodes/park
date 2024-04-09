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
                    movimentacaoForm.setIdCustomer(customerDto.getId());
                    vehicleDto = modelMapper.map(movimentacaoForm, VehicleDto.class);
                    vehicleDto = vehicleService.insert(vehicleDto); 
                } 
            } else if(movimentacaoForm.getIdCustomerType() == 2) {
                customerDto = customerService.findById(movimentacaoForm.getIdCustomer());
                vehicleDto = vehicleService.findById(movimentacaoForm.getIdVehicle());
            }

            ticketDto.setEntryTime(time);
            ticketDto.setExitTime(null);
            ticketDto.setIdCustomer(customerDto.getId());
            ticketDto.setIdVacancy(vacancyDto.getId());
            ticketDto.setIdVehicle(vehicleDto.getId());

            ticketDto = this.insert(ticketDto);

            return ticketDto;    
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível criar o Ticket!", e);
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

    public TicketDto finishMovimentacao(TicketForm ticketForm, Integer id) {
        try {
            Optional<TicketModel> ticketExist = ticketRepository.findById(id);
            LocalDateTime date = LocalDateTime.now();

            if (ticketExist.isPresent()) {
                ticketForm.setExitTime(date);
                TicketDto ticketDto = this.updateById(ticketForm, id);

                VacancyDto vacancyDto = vacancyService.findById(ticketDto.getIdVacancy());
                VacancyForm vacancyForm = modelMapper.map(vacancyDto, VacancyForm.class);
                vacancyForm.setSituation(true);
                vacancyDto = vacancyService.updateById(vacancyForm, ticketDto.getIdVacancy());

                return ticketDto;
            }else {
                throw new DataIntegrityException("O Id do Ticket não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).", e);
        }
    }
}
