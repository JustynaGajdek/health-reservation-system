package com.justynagajdek.healthreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrescriptionDto {
    private Long id;
    private Long appointmentId;
    private String medicineName;
    private String dosage;
    private String instructions;
    private LocalDateTime issuedAt;
}
