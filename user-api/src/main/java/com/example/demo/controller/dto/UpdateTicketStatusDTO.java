package com.example.demo.controller.dto;

import com.example.demo.repository.entity.TicketStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateTicketStatusDTO(
    @NotNull TicketStatus newStatus,
    @Email String responsibleEmail,
    String cancellationReason 
) {
}