package com.justynagajdek.healthreservationsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDto {
    private Long id;

    @NotBlank
    private String vaccineName;

    @NotNull
    private LocalDate vaccinationDate;

    @JsonProperty("mandatory")
    private boolean isMandatory;

    public VaccinationDto(String vaccineName, LocalDate vaccinationDate, boolean isMandatory) {
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.isMandatory = isMandatory;
    }

}
