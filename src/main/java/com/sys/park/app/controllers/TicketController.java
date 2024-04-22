package com.sys.park.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sys.park.app.dtos.Ticket.MovimentacaoDto;
import com.sys.park.app.dtos.Ticket.MovimentacaoForm;
import com.sys.park.app.dtos.Ticket.TicketDto;
import com.sys.park.app.dtos.Ticket.TicketForm;
import com.sys.park.app.services.TicketService;
import com.sys.park.app.services.exceptions.ConstraintException;
import com.sys.park.app.services.exceptions.DataIntegrityException;

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
    public ResponseEntity<Page<MovimentacaoDto>> findAllMov(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        try {
            Pageable pageable = Pageable.unpaged();

            if (page != null && size != null) {
                pageable = PageRequest.of(page, size);
            }

            Page<MovimentacaoDto> ticketDtoPage = ticketService.findAllMov(Optional.of(pageable));
            return ResponseEntity.ok().body(ticketDtoPage);     
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro de paginação");
        }
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
    
        TicketDto ticketDto = ticketService.createNewMov(modelMapper.map(movForm, MovimentacaoDto.class));
        
        return ResponseEntity.ok().body(ticketDto);
    }

    @PutMapping("/movimentacao/{id}")
    public ResponseEntity<TicketDto> updateMov(@Valid @RequestBody MovimentacaoForm movForm, 
        @PathVariable("id") Integer id, BindingResult br) {
            
        if (br.hasErrors()) {
            List<String> errors = br.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ConstraintException("Restrição de Dados", errors);
        }
    
        TicketDto ticketDto = ticketService.updateMovimentacao(movForm, id);
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
     

        TicketDto ticketDto = ticketService.updateById(modelMapper.map(ticketForm, TicketDto.class), id);
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
     
        TicketDto ticketDto = ticketService.finishMovimentacao(modelMapper.map(ticketForm, TicketDto.class), id);
        return ResponseEntity.ok().body(ticketDto);
    }
}

