package com.justynagajdek.healthreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDto {
    private Long appointmentId;
    private String medicineName;
    private String dosage;
    private String instructions;
}
