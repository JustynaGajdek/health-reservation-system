package com.justynagajdek.healthreservationsystem.dto;

import com.justynagajdek.healthreservationsystem.enums.AppointmentType;
import java.time.LocalDateTime;

public class AppointmentRequestDto {
    private LocalDateTime preferredDateTime;
    private AppointmentType appointmentType;

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
}
