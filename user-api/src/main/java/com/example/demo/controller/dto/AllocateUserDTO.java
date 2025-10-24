package com.example.demo.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AllocateUserDTO(
    @NotNull @Email String userEmail
) {
}