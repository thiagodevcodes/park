package com.sys.park.app.services;

import java.time.LocalDate;
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
import com.sys.park.app.dtos.Customer.CustomerMensalDto;
import com.sys.park.app.dtos.CustomerVehicle.CustomerVehicleDto;
import com.sys.park.app.dtos.Person.PersonDto;
import com.sys.park.app.dtos.Ticket.MovimentacaoDto;
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
    private TicketRepository ticketRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private PersonService personService;

    @Autowired
    private CustomerVehicleService customerVehicleService;
   
    @Autowired
    private ModelMapper modelMapper;

    public List<TicketDto> findAll() {
        try {
            List<TicketModel> ticketModelList = ticketRepository.findAll();

            return ticketModelList.stream()
                    .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar o Ticket!");
        }
    }

    public TicketDto findById(Integer id) {
        try {
            TicketModel ticketModel = ticketRepository.findById(id).get();
            return modelMapper.map(ticketModel, TicketDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + TicketModel.class.getName());
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

    public TicketDto createNewTicket(MovimentacaoDto movimentacaoDto) {
        try {
            validTicket(movimentacaoDto);

            CustomerDto customerDto = new CustomerDto();
            TicketDto ticketDto = new TicketDto();
            VehicleDto vehicleDto = new VehicleDto();
            CustomerMensalDto customerNew = new CustomerMensalDto();
            CustomerVehicleDto customerVehicleDto = new CustomerVehicleDto();

            Boolean vehicleIsPresent = vehicleService.verifyPlate(movimentacaoDto.getPlate());
            
            if(vehicleIsPresent) {
                vehicleDto = vehicleService.findByPlate(movimentacaoDto.getPlate());      
            }           

            if (movimentacaoDto.getIdCustomerType() == 1) {
                if (vehicleIsPresent && vehicleDto.getMonthlyVehicle()) {
                    throw new BusinessRuleException("Este veículo é mensalista");
                } 

                customerNew.setName(movimentacaoDto.getName());
                customerNew.setClientType(1);
                customerNew.setIsActive(true);

                customerNew = customerService.createNewCustomer(customerNew, customerNew.getClientType());
                customerDto = customerService.findById(customerNew.getId());

                if (!vehicleIsPresent) {
                    vehicleDto.setMake(movimentacaoDto.getMake());
                    vehicleDto.setModel(movimentacaoDto.getModel());
                    vehicleDto.setPlate(movimentacaoDto.getPlate());
    
                    vehicleDto = vehicleService.createVehicle(vehicleDto, false, customerDto.getId());        
                } else {
                    vehicleDto.setMonthlyVehicle(false);
                    vehicleDto = vehicleService.updateById(vehicleDto, vehicleDto.getId());
                }
            } else if (movimentacaoDto.getIdCustomerType() == 2) {
                customerDto = customerService.findById(movimentacaoDto.getIdCustomer());
                vehicleDto = vehicleService.findById(movimentacaoDto.getIdVehicle());
                vehicleDto.setMonthlyVehicle(true);
                vehicleDto = vehicleService.updateById(vehicleDto, vehicleDto.getId());
            }

            VacancyDto vacancyDto = vacancyService.findById(movimentacaoDto.getIdVacancy());
            vacancyDto.setSituation(false);
            vacancyService.updateById(vacancyDto, movimentacaoDto.getIdVacancy());
            
            if(customerVehicleService.vehicleCustomerExist(customerDto.getId(), vehicleDto.getId())) {
                customerVehicleDto = customerVehicleService.findByIdCustomerAndIdVehicle(customerDto.getId(), vehicleDto.getId());
            } else {
                customerVehicleDto.setIdCustomer(customerDto.getId());
                customerVehicleDto.setIdVehicle(vehicleDto.getId());
                customerVehicleDto = customerVehicleService.insert(customerVehicleDto);
            }

            ticketDto.setRegisterDate(LocalDate.now());
            ticketDto.setEntryTime(LocalDateTime.now());
            ticketDto.setExitTime(null);
            ticketDto.setIdVacancy(vacancyDto.getId());
            ticketDto.setIdCustomerVehicle(customerVehicleDto.getId());
            ticketDto.setIsActive(true);
            ticketDto = this.insert(ticketDto);
    
            return ticketDto;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível criar o Ticket!", e);
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

    public TicketDto finishTicket(TicketDto ticketDto, Integer id) {
        try {
            Optional<TicketModel> ticketExist = ticketRepository.findById(id);
            LocalDateTime date = LocalDateTime.now();

            if (ticketExist.isPresent()) {
                TicketModel ticket = ticketExist.get();
                ticket.setTotalPrice(ticketDto.getTotalPrice());
                ticket.setExitTime(date);
                ticket.setIsActive(false);
                ticketDto = modelMapper.map(ticket, TicketDto.class);

                ticketDto = this.updateById(ticketDto, id);
                
                VacancyDto vacancyDto = vacancyService.findById(ticketDto.getIdVacancy());
                vacancyDto.setSituation(true);
                vacancyDto = vacancyService.updateById(vacancyDto, ticketDto.getIdVacancy());
                
                return ticketDto;
            }else {
                throw new DataIntegrityException("O Id do Ticket não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Cliente não foi(foram) preenchido(s).", e);
        }
    }

    public TicketDto updateTicket(MovimentacaoDto movimentacaoDto, Integer id) {
        try {
            TicketDto ticketDto = this.findById(id);
            CustomerVehicleDto customerVehicleDto = customerVehicleService.findById(ticketDto.getIdCustomerVehicle());
            VehicleDto vehicleDto = vehicleService.findById(customerVehicleDto.getIdVehicle());
            CustomerDto customerDto = customerService.findById(customerVehicleDto.getIdCustomer());
            PersonDto personDto = personService.findById(customerDto.getIdPerson());
            VacancyDto vacancyDto = vacancyService.findById(ticketDto.getIdVacancy());
            
            if(movimentacaoDto.getIdCustomerType() == 1) {
                personDto.setName(movimentacaoDto.getName());
                personService.updateById(personDto, personDto.getId());
                
                if(!movimentacaoDto.getPlate().equals(vehicleDto.getPlate())) {
                    if(ticketIsActive(movimentacaoDto.getPlate())) {
                        vehicleService.validVehicle(movimentacaoDto.getPlate());
                    }
                }

                vehicleDto.setMake(movimentacaoDto.getMake());
                vehicleDto.setModel(movimentacaoDto.getModel());
                vehicleDto.setPlate(movimentacaoDto.getPlate());

                vehicleDto = vehicleService.updateById(vehicleDto, customerDto.getId());   

            } else if(movimentacaoDto.getIdCustomerType() == 2) {
                vehicleDto = vehicleService.findById(movimentacaoDto.getIdVehicle());
                customerDto = customerService.findById(movimentacaoDto.getIdCustomer());
                customerVehicleDto = customerVehicleService.findByIdCustomerAndIdVehicle(customerDto.getId(), vehicleDto.getId());
                
                if(this.ticketIsActive(vehicleDto.getPlate()) && !vehicleDto.getId().equals(customerVehicleDto.getIdVehicle())) {
                    throw new BusinessRuleException("Movimentação já existe!");
                }  
            }

            if(movimentacaoDto.getIdVacancy() != null) {
                vacancyDto.setSituation(true);
                vacancyService.updateById(vacancyDto, ticketDto.getIdVacancy());

                VacancyDto newVacancy = vacancyService.findById(movimentacaoDto.getIdVacancy());
                newVacancy.setSituation(false);
                vacancyService.updateById(newVacancy, movimentacaoDto.getIdVacancy());
            }

            ticketDto.setIdVacancy(movimentacaoDto.getIdVacancy());
            ticketDto.setIdCustomerVehicle(customerVehicleDto.getId());

            this.updateById(ticketDto, ticketDto.getId());
            
            return ticketDto;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não foi possível atualizar o ticket!", e);
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

    public Page<MovimentacaoDto> getAllTickets(Optional<Pageable> optionalPage) {
        try {
            Pageable page = optionalPage.orElse(Pageable.unpaged());
            Page<TicketModel> tickets = ticketRepository.findByIsActive(true, page);
            List<MovimentacaoDto> newDtoList = new ArrayList<>();           

            for (TicketModel ticket : tickets) {
                MovimentacaoDto newDto = new MovimentacaoDto();

                CustomerVehicleDto customerVehicleDto = customerVehicleService.findById(ticket.getIdCustomerVehicle());

                CustomerDto customerDto = customerService.findById(customerVehicleDto.getIdCustomer());
                VehicleDto vehicleDto = vehicleService.findById(customerVehicleDto.getIdVehicle());
                PersonDto personDto = personService.findById(customerDto.getIdPerson());
 
                if(ticket.getExitTime() == null) {
                    newDto.setId(ticket.getId());
                    newDto.setEntryTime(ticket.getEntryTime());
                    newDto.setExitTime(ticket.getExitTime());
                    newDto.setName(personDto.getName());
                    newDto.setMake(vehicleDto.getMake());
                    newDto.setModel(vehicleDto.getModel());
                    newDto.setPlate(vehicleDto.getPlate());
                    newDto.setIdVacancy(ticket.getIdVacancy());
                    newDto.setIdCustomerType(customerDto.getIdCustomerType());
                    newDto.setIdCustomer(null);
                    newDto.setIdVehicle(null);
                    newDtoList.add(newDto);
                }
            } 

            Page<MovimentacaoDto> pageableMov = new PageImpl<>(newDtoList, page, tickets.getTotalElements());
            return pageableMov;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível listar os Tickets!");
        }
    }

    private void validTicket(MovimentacaoDto movimentacaoDto) {
        try {
            if(movimentacaoDto.getIdCustomerType() == 2) {
                VehicleDto vehicleDto = vehicleService.findById(movimentacaoDto.getIdVehicle());
                movimentacaoDto.setPlate(vehicleDto.getPlate());
            }

            if (this.ticketIsActive(movimentacaoDto.getPlate())) {
                throw new BusinessRuleException("Movimentação já existe");
            }      
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro ao validar o ticket", e);
        }
    }

    private Boolean ticketIsActive(String plate) {
        try {
                List<TicketModel> ticketModels = ticketRepository.findByIsActive(true);
                Boolean isPresent = false;

                for (TicketModel ticket: ticketModels) {
                    CustomerVehicleDto customerVehicleDto = customerVehicleService.findById(ticket.getIdCustomerVehicle());
                    VehicleDto vehicle = vehicleService.findById(customerVehicleDto.getIdVehicle());

                    System.out.println(vehicle);
                    System.out.println(plate);

                    if(vehicle.getPlate().equals(plate)) {
                        isPresent = true;
                    } 
                }
    
                return isPresent;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro ao verificar se o ticket está ativo!", e);
        }
    }

    public List<TicketDto> findByRegisterDateAndIsActive() {
        try {
            List<TicketModel> ticketModelList = ticketRepository.findByRegisterDateAndIsActive(LocalDate.now(), false);
            
            return ticketModelList.stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .collect(Collectors.toList());
            
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não foi possível buscar os dados!");
        }
    }
}
