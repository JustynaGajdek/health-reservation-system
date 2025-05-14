package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.VaccinationDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import com.justynagajdek.healthreservationsystem.mapper.VaccinationMapper;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.VaccinationRepository;
import org.springframework.stereotype.Service;

@Service
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final PatientRepository patientRepository;
    private final VaccinationMapper vaccinationMapper;

    public VaccinationService(VaccinationRepository vaccinationRepository,
                              PatientRepository patientRepository,
                              VaccinationMapper vaccinationMapper) {
        this.vaccinationRepository = vaccinationRepository;
        this.patientRepository = patientRepository;
        this.vaccinationMapper = vaccinationMapper;
    }

    public void addVaccination(Long patientId, VaccinationDto dto) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        VaccinationEntity vaccination = vaccinationMapper.toEntity(dto);
        vaccination.setPatient(patient);

        vaccinationRepository.save(vaccination);
    }
}
