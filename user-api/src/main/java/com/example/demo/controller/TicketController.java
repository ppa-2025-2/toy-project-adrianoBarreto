package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.NewTicketDTO;
import com.example.demo.controller.dto.UpdateTicketStatusDTO;
import com.example.demo.domain.TicketBusiness;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.entity.Ticket;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    
    private final TicketBusiness ticketBusiness;
    private final TicketRepository ticketRepository;

    public TicketController(TicketBusiness ticketBusiness, TicketRepository ticketRepository) {
        this.ticketBusiness = ticketBusiness;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Ticket> createTicket(@RequestBody NewTicketDTO dto) {
        Ticket newTicket = ticketBusiness.createTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTicket);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Integer id) {
        return ticketRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateStatus(
        @PathVariable Integer id, 
        @RequestBody UpdateTicketStatusDTO dto
    ) {
        Ticket updatedTicket = ticketBusiness.updateTicketStatus(id, dto);
        return ResponseEntity.ok(updatedTicket);
    }
}