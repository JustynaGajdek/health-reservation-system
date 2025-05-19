package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.VaccinationDto;
import com.justynagajdek.healthreservationsystem.service.VaccinationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaccinations")
public class VaccinationController {

    private final VaccinationService vaccinationService;

    public VaccinationController(VaccinationService vaccinationService) {
        this.vaccinationService = vaccinationService;
    }

    @PostMapping("/patient/{id}")
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVaccination(@PathVariable Long id,
                               @RequestBody @Valid VaccinationDto dto) {
        vaccinationService.addVaccination(id, dto);
    }

    @GetMapping("/patient")
    @PreAuthorize("hasRole('PATIENT')")
    public List<VaccinationDto> getMyVaccinations() {
        return vaccinationService.getVaccinationsForCurrentPatient();
    }
}

