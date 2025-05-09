package com.justynagajdek.healthreservationsystem.dto;

import java.time.LocalDateTime;

public class AssignAppointmentDto {
    private Long doctorId;
    private LocalDateTime appointmentDate;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
