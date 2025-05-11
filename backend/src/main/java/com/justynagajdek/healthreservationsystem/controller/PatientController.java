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


}
