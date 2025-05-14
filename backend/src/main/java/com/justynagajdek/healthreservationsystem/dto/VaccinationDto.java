package com.justynagajdek.healthreservationsystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccinationDto {
    private Long id;
    private String vaccineName;
    private LocalDate vaccinationDate;
    private boolean isMandatory;
}
