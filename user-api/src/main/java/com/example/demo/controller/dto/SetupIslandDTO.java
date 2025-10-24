package com.example.demo.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SetupIslandDTO(
    @NotBlank String islandName,
    @Min(1) int workstationCount
) {
}
