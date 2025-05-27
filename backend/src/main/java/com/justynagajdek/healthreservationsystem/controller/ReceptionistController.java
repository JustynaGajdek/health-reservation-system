package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.AppointmentCreationDto;
import com.justynagajdek.healthreservationsystem.dto.AssignAppointmentDto;
import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.service.AppointmentService;
import com.justynagajdek.healthreservationsystem.service.PatientService;
import com.justynagajdek.healthreservationsystem.service.PrescriptionService;
import com.justynagajdek.healthreservationsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reception")
@PreAuthorize("hasRole('RECEPTIONIST')")
public class ReceptionistController {

    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    private final UserService userService;
    private final PatientService patientService;


    public ReceptionistController(AppointmentService appointmentService, PrescriptionService prescriptionService,
                                  UserService userService, PatientService patientService) {
        this.appointmentService = appointmentService;
        this.prescriptionService = prescriptionService;
        this.userService = userService;
        this.patientService = patientService;
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

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/receptionist/users/reject/{id}")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<String> rejectUser(@PathVariable Long id) {
        userService.rejectUser(id);
        return ResponseEntity.ok("User rejected");
    }

    @GetMapping("/users/pending")
    public ResponseEntity<List<?>> getPendingUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🔍 Authenticated user: " + auth.getName());
        System.out.println("🔍 Authorities:     " + auth.getAuthorities());

        var pendingUsers = userService.getPendingUsers();
        return ResponseEntity.ok(pendingUsers);
    }

    @PostMapping("/patients")
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto dto) {
        PatientDto created = patientService.createPatient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }





}
