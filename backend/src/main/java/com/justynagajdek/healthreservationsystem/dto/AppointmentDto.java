package com.justynagajdek.healthreservationsystem.dto;

import com.justynagajdek.healthreservationsystem.enums.AppointmentStatus;
import com.justynagajdek.healthreservationsystem.enums.AppointmentType;

import java.time.LocalDateTime;

public class AppointmentDto {
    private Long id;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private AppointmentType appointmentType;
    private String doctorName;
    private String patientName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public AppointmentType getAppointmentType() { return appointmentType; }
    public void setAppointmentType(AppointmentType appointmentType) { this.appointmentType = appointmentType; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
}
