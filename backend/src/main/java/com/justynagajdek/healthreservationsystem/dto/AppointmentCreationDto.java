package com.justynagajdek.healthreservationsystem.dto;

import com.justynagajdek.healthreservationsystem.enums.AppointmentType;

import java.time.LocalDateTime;

public class AppointmentCreationDto {
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentDate;
    private AppointmentType appointmentType;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public AppointmentType getAppointmentType() { return appointmentType; }
    public void setAppointmentType(AppointmentType appointmentType) { this.appointmentType = appointmentType; }

    public void setAppointmentDateTime(String s) {

    }
}
