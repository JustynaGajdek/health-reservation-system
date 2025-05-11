package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.PrescriptionEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.mapper.PrescriptionMapper;
import com.justynagajdek.healthreservationsystem.repository.AppointmentRepository;
import com.justynagajdek.healthreservationsystem.repository.PrescriptionRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionMapper mapper;
    private final UserRepository userRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               AppointmentRepository appointmentRepository,
                               PrescriptionMapper mapper,  UserRepository userRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    public PrescriptionDto createPrescription(PrescriptionDto dto) {
        AppointmentEntity appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        PrescriptionEntity entity = mapper.toEntity(dto, appointment);
        PrescriptionEntity saved = prescriptionRepository.save(entity);

        return mapper.toDto(saved);
    }

    public List<PrescriptionEntity> getPrescriptionsForAppointment(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    public List<PrescriptionDto> getPrescriptionsForCurrentPatient(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        PatientEntity patient = user.getPatient();
        if (patient == null) {
            throw new IllegalStateException("No patient record linked to user");
        }

        List<PrescriptionEntity> prescriptions = prescriptionRepository.findByAppointmentPatient(patient);
        return prescriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PrescriptionDto> getPrescriptionsByPatientId(Long patientId) {
        PatientEntity patient = userRepository.findById(patientId)
                .map(UserEntity::getPatient)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        List<PrescriptionEntity> prescriptions = prescriptionRepository.findByAppointmentPatient(patient);
        return prescriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}
