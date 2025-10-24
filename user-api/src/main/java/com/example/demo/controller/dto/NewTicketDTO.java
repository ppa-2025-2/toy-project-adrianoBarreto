package com.example.demo.controller.dto;

import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewTicketDTO(
    @NotNull @Email String creatorEmail,
    @NotNull @Email String recipientEmail,
    List<@Email String> observerEmails,
    
    @NotBlank String subject,
    @NotBlank String action,
    String details,
    String location
) {
}