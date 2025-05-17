package com.justynagajdek.healthreservationsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.justynagajdek.healthreservationsystem.enums.AppointmentType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentRequestDto {
    @NotNull(message = "Date and time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime preferredDateTime;
    @NotNull
    private AppointmentType appointmentType;
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;


    // Getters and setters
    public LocalDateTime getPreferredDateTime() {
        return preferredDateTime;
    }

    public void setPreferredDateTime(LocalDateTime preferredDateTime) {
        this.preferredDateTime = preferredDateTime;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}
