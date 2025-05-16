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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("Only patients can request appointments.");
        }

        DoctorEntity doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email).orElseThrow();

        if (user.getPatient() != null) {
            return appointmentRepository.findByPatient_Id(user.getPatient().getId());
        } else if (user.getDoctor() != null) {
            return appointmentRepository.findByDoctor_Id(user.getDoctor().getId());
        } else {
            throw new RuntimeException("User is neither a doctor nor a patient.");
        }
    }




}