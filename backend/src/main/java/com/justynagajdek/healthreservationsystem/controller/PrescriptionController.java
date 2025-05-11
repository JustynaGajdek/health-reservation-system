package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.PrescriptionEntity;
import com.justynagajdek.healthreservationsystem.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor/prescriptions")
@PreAuthorize("hasRole('DOCTOR')")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionEntity> createPrescription(@RequestBody PrescriptionDto dto) {
        PrescriptionEntity saved = prescriptionService.createPrescription(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionEntity>> getPrescriptionsForAppointment(
            @RequestParam Long appointmentId) {
        List<PrescriptionEntity> list = prescriptionService.getPrescriptionsForAppointment(appointmentId);
        return ResponseEntity.ok(list);
    }
}
