package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.AppointmentCreationDto;
import com.justynagajdek.healthreservationsystem.dto.AssignAppointmentDto;
import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.service.AppointmentService;
import com.justynagajdek.healthreservationsystem.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reception")
@PreAuthorize("hasRole('RECEPTIONIST')")
public class ReceptionistController {

    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;


    public ReceptionistController(AppointmentService appointmentService, PrescriptionService prescriptionService) {
        this.appointmentService = appointmentService;
        this.prescriptionService = prescriptionService;
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

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping("/appointments")
    public ResponseEntity<String> createAppointmentOnSite(@RequestBody AppointmentCreationDto dto) {
        appointmentService.createConfirmedAppointment(dto);
        return ResponseEntity.ok("Appointment created and confirmed.");
    }


}
