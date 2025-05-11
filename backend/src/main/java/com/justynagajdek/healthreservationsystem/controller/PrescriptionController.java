package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.PrescriptionEntity;
import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import com.justynagajdek.healthreservationsystem.mapper.PrescriptionMapper;
import com.justynagajdek.healthreservationsystem.service.PrescriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionController(PrescriptionService prescriptionService, JwtTokenUtil jwtTokenUtil,PrescriptionMapper prescriptionMapper) {
        this.prescriptionService = prescriptionService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.prescriptionMapper = prescriptionMapper;
    }


    // Lekarz – tworzenie recepty
    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping
    public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody PrescriptionDto dto) {
        PrescriptionDto saved = prescriptionService.createPrescription(dto);
        return ResponseEntity.ok(saved);
    }
    // Lekarz – pobranie recept dla wizyty
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/by-appointment")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsForAppointment(@RequestParam Long appointmentId) {
        var list = prescriptionService.getPrescriptionsForAppointment(appointmentId).stream()
                .map(prescriptionMapper::toDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    // Pacjent – pobranie swoich recept
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/my")
    public ResponseEntity<List<PrescriptionDto>> getMyPrescriptions(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = jwtTokenUtil.getUsernameFromJwtToken(token);
        List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsForCurrentPatient(email);
        return ResponseEntity.ok(prescriptions);
    }

    // Recepcjonista – pobranie recept pacjenta
    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/by-patient")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByPatient(@RequestParam Long patientId) {
        List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(prescriptions);
    }
}
