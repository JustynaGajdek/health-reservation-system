package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.AssignAppointmentDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reception")
@PreAuthorize("hasRole('RECEPTIONIST')")
public class ReceptionistController {

    private final AppointmentService appointmentService;

    public ReceptionistController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments/unassigned")
    public List<AppointmentEntity> getUnassignedAppointments() {
        return appointmentService.getUnassignedAppointments();
    }

    @PutMapping("/appointments/{id}/assign")
    public ResponseEntity<String> assignAppointment(@PathVariable Long id,
                                                    @RequestBody AssignAppointmentDto dto) {
        appointmentService.assignDoctorAndConfirm(id, dto);
        return ResponseEntity.ok("Appointment assigned and confirmed.");
    }
}
