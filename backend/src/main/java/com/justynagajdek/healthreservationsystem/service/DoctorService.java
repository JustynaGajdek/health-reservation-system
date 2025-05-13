package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.AppointmentDto;
import com.justynagajdek.healthreservationsystem.dto.DoctorDto;
import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.mapper.AppointmentMapper;
import com.justynagajdek.healthreservationsystem.mapper.DoctorMapper;
import com.justynagajdek.healthreservationsystem.mapper.PatientMapper;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;
    private final AppointmentMapper appointmentMapper;
    private final PatientMapper patientMapper;

    public DoctorService(UserRepository userRepository,
                         DoctorMapper doctorMapper,
                         AppointmentMapper appointmentMapper,
                         PatientMapper patientMapper) {
        this.userRepository = userRepository;
        this.doctorMapper = doctorMapper;
        this.appointmentMapper = appointmentMapper;
        this.patientMapper = patientMapper;
    }

    public DoctorDto getCurrentDoctorProfile() {
        DoctorEntity doctor = getCurrentDoctor();
        return doctorMapper.toDto(doctor);
    }

    public List<AppointmentDto> getAppointmentsForCurrentDoctor() {
        DoctorEntity doctor = getCurrentDoctor();
        return doctor.getAppointments()
                .stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    public PatientDto getPatientInfo(Long patientId) {
        DoctorEntity doctor = getCurrentDoctor(); // opcjonalna walidacja dostępu
        return patientMapper.toDto(
                doctor.getAppointments().stream()
                        .map(a -> a.getPatient())
                        .filter(p -> p.getId().equals(patientId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Patient not found or not assigned to you"))
        );
    }

    private DoctorEntity getCurrentDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        if (user.getDoctor() == null) {
            throw new IllegalStateException("Current user is not a doctor");
        }

        return user.getDoctor();
    }
}
