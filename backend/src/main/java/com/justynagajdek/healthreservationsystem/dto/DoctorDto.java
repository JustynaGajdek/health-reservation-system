package com.justynagajdek.healthreservationsystem.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private Long id;
    private String fullName;
    private String email;
    private String specialization;
    private String officeNumber;
    private String workingHours;

}
