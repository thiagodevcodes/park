package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Ticket.TicketDto;
import com.sys.park.app.dtos.Ticket.TicketForm;
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

    public TicketDto insert(TicketForm ticketForm) {
        try {
            TicketModel newTicket = modelMapper.map(ticketForm, TicketModel.class);
            
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
}
