package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.exception.ResourceNotFoundException;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PatientEntity registerPatient(SignUpDto dto) {

        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhone());
        user.setRole(Role.PATIENT);
        user.setStatus(AccountStatus.PENDING);
        user = userRepository.save(user);

        PatientEntity p = new PatientEntity();
        p.setUser(user);
        p.setPesel(dto.getPesel());
        p.setDateOfBirth(dto.getDateOfBirth());
        p.setAddress(dto.getAddress());
        return patientRepository.save(p);
    }

    @Transactional(readOnly = true)
    public PatientEntity getMyProfile(String email) {
        return patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No patient profile for user " + email));
    }

    @Transactional
    public PatientEntity updateProfile(String email, PatientDto dto) {
        PatientEntity p = getMyProfile(email);
        UserEntity u = p.getUser();
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setPhoneNumber(dto.getPhoneNumber());
        userRepository.save(u);

        p.setDateOfBirth(dto.getDateOfBirth());
        p.setAddress(dto.getAddress());
        return patientRepository.save(p);
    }

    @Transactional(readOnly = true)
    public PatientEntity getByPesel(String pesel) {
        return patientRepository.findByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient with PESEL " + pesel + " not found"));
    }

    @Transactional
    public PatientDto createPatient(PatientDto dto) {
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());

        user.setPasswordHash(passwordEncoder.encode("defaultPass123"));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(Role.PATIENT);
        user.setStatus(AccountStatus.ACTIVE);
        user = userRepository.save(user);

        PatientEntity patient = new PatientEntity();
        patient.setUser(user);
        patient.setPesel(dto.getPesel());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setAddress(dto.getAddress());
        patient = patientRepository.save(patient);

        PatientDto out = new PatientDto();
        out.setId(user.getId());
        out.setEmail(user.getEmail());
        out.setFirstName(user.getFirstName());
        out.setLastName(user.getLastName());
        out.setPhoneNumber(user.getPhoneNumber());
        out.setPesel(patient.getPesel());
        out.setDateOfBirth(patient.getDateOfBirth());
        out.setAddress(patient.getAddress());
        return out;
    }

    @Transactional(readOnly = true)
    public List<PatientEntity> getAllPatients() {
        return patientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PatientEntity getById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found with id: " + id)
                );
    }

}
