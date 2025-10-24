package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;
import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewTicketDTO;
import com.example.demo.controller.dto.UpdateTicketStatusDTO;
import com.example.demo.domain.stereotype.Business;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Ticket;
import com.example.demo.repository.entity.TicketStatus;
import com.example.demo.repository.entity.User;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Business
@Validated
public class TicketBusiness {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketBusiness(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Ticket createTicket(@Valid NewTicketDTO dto) {
        User creator = userRepository.findByEmail(dto.creatorEmail())
            .orElseThrow(() -> new IllegalArgumentException("Usuário criador não encontrado: " + dto.creatorEmail()));
        
        User recipient = userRepository.findByEmail(dto.recipientEmail())
            .orElseThrow(() -> new IllegalArgumentException("Usuário destinatário não encontrado: " + dto.recipientEmail()));

        Set<User> observers = new HashSet<>();
        if (dto.observerEmails() != null) {
            for (String email : dto.observerEmails()) {
                User observer = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário observador não encontrado: " + email));
                observers.add(observer);
            }
        }

        Ticket ticket = new Ticket();
        ticket.setCreator(creator);
        ticket.setRecipient(recipient);
        ticket.setObservers(observers);
        ticket.setSubject(dto.subject());
        ticket.setAction(dto.action());
        ticket.setDetails(dto.details());
        ticket.setLocation(dto.location());
        ticket.setStatus(TicketStatus.PENDENTE);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket updateTicketStatus(Integer ticketId, @Valid UpdateTicketStatusDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado: " + ticketId));

        switch (dto.newStatus()) {
            case ANDAMENTO:
                if (dto.responsibleEmail() == null || dto.responsibleEmail().isBlank()) {
                    throw new IllegalArgumentException("Para mover para 'ANDAMENTO', um responsável é obrigatório.");
                }
                User responsible = userRepository.findByEmail(dto.responsibleEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário responsável não encontrado: " + dto.responsibleEmail()));
                ticket.setResponsible(responsible);
                ticket.setCancellationReason(null);
                break;
            
            case CANCELADO:
                if (dto.cancellationReason() == null || dto.cancellationReason().isBlank()) {
                    throw new IllegalArgumentException("Para 'CANCELAR', um motivo é obrigatório.");
                }
                ticket.setCancellationReason(dto.cancellationReason());
                ticket.setResponsible(null);
                break;

            case CONCLUIDO:
                if (ticket.getResponsible() == null) {
                    throw new IllegalArgumentException("Um ticket só pode ser 'CONCLUÍDO' se tiver um responsável (status 'ANDAMENTO' prévio).");
                }
                ticket.setCancellationReason(null);
                break;
            
            case PENDENTE:
                ticket.setResponsible(null);
                ticket.setCancellationReason(null);
                break;
        }

        ticket.setStatus(dto.newStatus());
        return ticketRepository.save(ticket);
    }
}