package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import com.justynagajdek.healthreservationsystem.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final PrescriptionService prescriptionService;
    private final JwtTokenUtil jwtTokenUtil;

    public PatientController(PrescriptionService prescriptionService, JwtTokenUtil jwtTokenUtil) {
        this.prescriptionService = prescriptionService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDto>> getMyPrescriptions(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = jwtTokenUtil.getUsernameFromJwtToken(token);
        List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsForCurrentPatient(email);
        return ResponseEntity.ok(prescriptions);
    }
}
