package com.justynagajdek.healthreservationsystem.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String pesel;
    private LocalDate dateOfBirth;
    private String address;
}