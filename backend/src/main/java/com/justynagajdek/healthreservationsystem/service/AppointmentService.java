package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.AppointmentCreationDto;
import com.justynagajdek.healthreservationsystem.dto.AppointmentRequestDto;
import com.justynagajdek.healthreservationsystem.dto.AssignAppointmentDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AppointmentStatus;
import com.justynagajdek.healthreservationsystem.repository.AppointmentRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import com.justynagajdek.healthreservationsystem.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public AppointmentEntity createPendingAppointment(AppointmentRequestDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPatient() == null) {
            throw new AccessDeniedException("Only patients can request appointments.");
        }

        if (dto.getPreferredDateTime().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot book an appointment in the past.");
        }

        DoctorEntity doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (appointmentRepository.existsByAppointmentDateAndDoctorId(dto.getPreferredDateTime(), doctor.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Appointment already exists at this time");
        }


        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setPatient(user.getPatient());
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getPreferredDateTime());
        appointment.setAppointmentType(dto.getAppointmentType());
        appointment.setStatus(AppointmentStatus.PENDING);


        return appointmentRepository.save(appointment);
    }

    public void assignDoctorAndConfirm(Long appointmentId, AssignAppointmentDto dto) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        DoctorEntity doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        appointmentRepository.save(appointment);
    }

    public List<AppointmentEntity> getUnassignedAppointments() {
        return appointmentRepository.findByDoctorIsNull();
    }

    public void createConfirmedAppointment(AppointmentCreationDto dto) {
        PatientEntity patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"))
                .getPatient();

        DoctorEntity doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentType(dto.getAppointmentType());
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        appointmentRepository.save(appointment);
    }

    public List<AppointmentEntity> getAppointmentsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated.");
        }

        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return switch (user.getRole()) {
            case PATIENT -> appointmentRepository.findAllByPatient_User(user);
            case DOCTOR -> appointmentRepository.findAllByDoctor_User(user);
            default -> throw new AccessDeniedException("Role " + user.getRole() + " not allowed to view appointments.");
        };
    }

    public void cancelAppointment(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found: " + id));
        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);
    }

    public void requestAppointmentCancellation(Long appointmentId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (currentUser.getPatient() == null || !appointment.getPatient().getId().equals(currentUser.getPatient().getId())) {
            throw new AccessDeniedException("You are not authorized to request cancellation for this appointment.");
        }

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only confirmed appointments can be cancellation-requested.");
        }

        appointment.setStatus(AppointmentStatus.CANCEL_REQUESTED);
        appointmentRepository.save(appointment);
    }


}