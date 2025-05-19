package com.justynagajdek.healthreservationsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccinationDto {
    private Long id;

    @NotBlank
    private String vaccineName;

    @NotNull
    private LocalDate vaccinationDate;

    @JsonProperty("mandatory")
    private boolean isMandatory;
}
