package com.sys.park.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.Ticket.MovimentacaoDto;
import com.sys.park.app.dtos.Ticket.MovimentacaoForm;
import com.sys.park.app.dtos.Ticket.TicketDto;
import com.sys.park.app.dtos.Ticket.TicketForm;
import com.sys.park.app.services.TicketService;
import com.sys.park.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> find(@PathVariable("id") Integer id) {        
        TicketDto personDto = ticketService.findById(id);
        return ResponseEntity.ok().body(personDto);
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> findAll() {
        List<TicketDto> userDtoList = ticketService.findAll();
        return ResponseEntity.ok().body(userDtoList);
    }

    @GetMapping("/movimentacao")
    public ResponseEntity<List<MovimentacaoDto>> findMov() {
        List<MovimentacaoDto> userDtoList = ticketService.findAllMov();
        return ResponseEntity.ok().body(userDtoList);
    }

    @PostMapping
    public ResponseEntity<TicketDto> insert(@Valid @RequestBody TicketForm ticketForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Restrição de Dados", errors);
        }

        TicketDto ticketDto = ticketService.insert(modelMapper.map(ticketForm, TicketDto.class));
        return ResponseEntity.ok().body(ticketDto);
    }

    @PostMapping("/movimentacao")
    public ResponseEntity<TicketDto> insert(@Valid @RequestBody MovimentacaoForm movForm, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Restrição de Dados", errors);
        }

        TicketDto ticketDto = ticketService.createNewMov(movForm);
        
        return ResponseEntity.ok().body(ticketDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> update(@Valid @RequestBody
        TicketForm ticketForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Restrição de Dados", errors);
        }
     

        TicketDto ticketDto = ticketService.updateById(ticketForm, id);
        return ResponseEntity.ok().body(ticketDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<TicketDto> finish(@Valid @RequestBody
        TicketForm ticketForm, @PathVariable("id") Integer id, BindingResult br) {
       
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Restrição de Dados", errors);
        }
     
        TicketDto ticketDto = ticketService.finishMovimentacao(ticketForm, id);
        return ResponseEntity.ok().body(ticketDto);
    }
}

