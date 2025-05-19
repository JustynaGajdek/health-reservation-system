package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.VaccinationDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import com.justynagajdek.healthreservationsystem.mapper.VaccinationMapper;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import com.justynagajdek.healthreservationsystem.repository.VaccinationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final PatientRepository patientRepository;
    private final VaccinationMapper vaccinationMapper;
    private final UserRepository userRepository;

    public VaccinationService(VaccinationRepository vaccinationRepository,
                              PatientRepository patientRepository,
                              VaccinationMapper vaccinationMapper,
                              UserRepository userRepository) {
        this.vaccinationRepository = vaccinationRepository;
        this.patientRepository = patientRepository;
        this.vaccinationMapper = vaccinationMapper;
        this.userRepository = userRepository;
    }

    public void addVaccination(Long patientId, VaccinationDto dto) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Patient not found"));

        VaccinationEntity vaccination = vaccinationMapper.toEntity(dto);
        vaccination.setPatient(patient);

        vaccinationRepository.save(vaccination);
    }

    public List<VaccinationDto> getVaccinationsForCurrentPatient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getPatient() == null) {
            throw new IllegalStateException("Current user is not a patient");
        }

        Long patientId = user.getPatient().getId();

        return vaccinationRepository.findByPatientId(patientId).stream()
                .map(vaccinationMapper::toDto)
                .toList();
    }

}
