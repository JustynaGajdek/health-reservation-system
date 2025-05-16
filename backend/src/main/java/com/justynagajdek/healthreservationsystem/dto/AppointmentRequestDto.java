package com.justynagajdek.healthreservationsystem.dto;

import com.justynagajdek.healthreservationsystem.enums.AppointmentType;
import java.time.LocalDateTime;

public class AppointmentRequestDto {
    private LocalDateTime preferredDateTime;
    private AppointmentType appointmentType;
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
