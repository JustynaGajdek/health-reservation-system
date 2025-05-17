package com.justynagajdek.healthreservationsystem.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DoctorDto {
    private Long id;
    private String fullName;
    private String email;
    private String specialization;
    private String officeNumber;
    private Map<String, String> workingHours;

}
