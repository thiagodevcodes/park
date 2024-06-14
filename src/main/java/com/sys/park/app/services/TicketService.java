package com.sys.park.app.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Customer.CustomerDto;
import com.sys.park.app.dtos.Customer.CustomerForm;
import com.sys.park.app.dtos.Ticket.TicketDto;
import com.sys.park.app.dtos.Ticket.TicketForm;
import com.sys.park.app.dtos.Vacancy.VacancyDto;
import com.sys.park.app.dtos.Vehicle.VehicleDto;
import com.sys.park.app.dtos.Vehicle.VehicleForm;
import com.sys.park.app.models.CustomerModel;
import com.sys.park.app.models.CustomerVehicleModel;
import com.sys.park.app.models.TicketModel;
import com.sys.park.app.repositories.CustomerRepository;
import com.sys.park.app.repositories.CustomerVehicleRepository;
import com.sys.park.app.repositories.TicketRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerVehicleRepository customerVehicleRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VacancyService vacancyService;
   
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

    public TicketDto updateById(TicketDto ticketDto, Integer id) {
        try {
            Optional<TicketModel> ticketExist = ticketRepository.findById(id);

            if (ticketExist.isPresent()) {
                TicketModel ticketUpdated = ticketExist.get();
                
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(ticketDto, ticketUpdated);   
                
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

    public TicketDto addNewTicket(TicketForm ticketForm) {
        try {           
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            TicketDto ticket = modelMapper.map(ticketForm, TicketDto.class);
            CustomerForm customer = ticketForm.getCustomer();
            VehicleForm vehicle = ticketForm.getVehicle(); 

            if(ticketRepository.existsActiveTicketByVehiclePlate(vehicle.getPlate())) {
                throw new DataIntegrityException("Já existe um ticket ativo!");
            }

            CustomerDto customerDto = customerService.addCustomer(customer);
            VehicleDto vehicleDto = vehicleService.insert(vehicle);
            CustomerVehicleModel customerVehicle = new CustomerVehicleModel();
            
            customerVehicle.setIdCustomer(customerDto.getId());
            customerVehicle.setIdVehicle(vehicleDto.getId());

            if(customerVehicleRepository.existsByIdCustomerAndIdVehicle(customerDto.getId(), vehicleDto.getId())) {
                customerVehicle = customerVehicleRepository.findByIdCustomerAndIdVehicle(customerDto.getId(),vehicleDto.getId()).get();
            } else {
                customerVehicle = customerVehicleRepository.save(customerVehicle);
            }   

            ticket.setIdCustomerVehicle(customerVehicle.getId());
            ticket.setIsActive(true);
            ticket.setEntryTime(LocalDateTime.now());
        
            TicketModel newTicket = ticketRepository.save(modelMapper.map(ticket, TicketModel.class));

            return modelMapper.map(newTicket, TicketDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Ticket não foi(foram) preenchido(s).");
        }
    }

    public TicketDto finishTicket(TicketForm ticketForm, Integer id) {
        TicketModel ticketModel = ticketRepository.findById(id).get();
        ticketModel.setIsActive(false);
        
        CustomerVehicleModel customerVehicleModel = customerVehicleRepository.findById(ticketModel.getIdCustomerVehicle()).get(); 
        CustomerModel customerModel = customerRepository.findById(customerVehicleModel.getIdCustomer()).get();
        customerModel.setIsActive(false);
        
        customerRepository.save(customerModel);
        ticketRepository.save(ticketModel);

        return modelMapper.map(ticketModel, TicketDto.class);
    }
}
