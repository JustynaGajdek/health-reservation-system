package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.AppointmentRequestDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@PreAuthorize("hasRole('PATIENT')")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PostMapping("/request")
    public ResponseEntity<String> requestAppointment(@RequestBody AppointmentRequestDto dto) {
        appointmentService.createPendingAppointment(dto);
        return ResponseEntity.ok("Appointment request submitted and pending assignment.");
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    @GetMapping("/mine")
    public List<AppointmentEntity> getMyAppointments() {
        return appointmentService.getAppointmentsForCurrentUser();
    }

}
