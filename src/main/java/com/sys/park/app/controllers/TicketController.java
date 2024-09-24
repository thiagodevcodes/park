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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.sys.park.app.dtos.Ticket.TicketDto;
import com.sys.park.app.dtos.Ticket.TicketRequest;
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

    @GetMapping
    public ResponseEntity<List<TicketDto>> findAll() {
        List<TicketDto> userDtoList = ticketService.findAll();
        return ResponseEntity.ok().body(userDtoList);
    }

    @GetMapping("find")
    public ResponseEntity<TicketDto> findById(@RequestParam("id") Long id) {        
        TicketDto personDto = ticketService.findById(id);
        return ResponseEntity.ok().body(personDto);
    }

    // @PostMapping
    // public ResponseEntity<TicketDto> insert(@Valid @RequestBody TicketRequest ticketForm, BindingResult br) {
            
    //     if (br.hasErrors()) {
    //         List<String> errors = br.getAllErrors().stream()
    //                 .map(DefaultMessageSourceResolvable::getDefaultMessage)
    //                 .collect(Collectors.toList());

    //         throw new ConstraintException("Restrição de Dados", errors);
    //     }

    //     TicketDto ticketDto = ticketService.addNewTicket(ticketForm);
    //     return ResponseEntity.ok().body(ticketDto);
    // }

    @PutMapping
    public ResponseEntity<TicketDto> updateById(@Valid @RequestBody
        TicketDto ticketForm, @RequestParam("id") Long id, BindingResult br) {
       
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

    @PutMapping("/finish")
    public ResponseEntity<TicketDto> finishTicket(@Valid @RequestBody
    TicketRequest ticketForm, @RequestParam("id") Long id, BindingResult br) {
        if (br.hasErrors()) {
            List<String> errors = new ArrayList<>();
            br.getAllErrors().forEach(e -> {
                errors.add(e.getDefaultMessage());
            });

            throw new ConstraintException("Restrição de Dados", errors);
        }
    
        TicketDto ticketDto = ticketService.finishTicket(null, id);
        return ResponseEntity.ok().body(ticketDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(@RequestParam("id") Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

