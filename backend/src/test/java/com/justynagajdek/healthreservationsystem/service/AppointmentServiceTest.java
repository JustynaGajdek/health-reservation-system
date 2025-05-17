package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.AppointmentCreationDto;
import com.justynagajdek.healthreservationsystem.dto.AppointmentRequestDto;
import com.justynagajdek.healthreservationsystem.dto.AssignAppointmentDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AppointmentStatus;
import com.justynagajdek.healthreservationsystem.enums.AppointmentType;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.AppointmentRepository;
import com.justynagajdek.healthreservationsystem.repository.DoctorRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private AppointmentRepository appointmentRepository;
    private UserRepository userRepository;
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        userRepository = mock(UserRepository.class);
        doctorRepository = mock(DoctorRepository.class);
        appointmentService = new AppointmentService(appointmentRepository, userRepository, doctorRepository);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("patient@example.com");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }

    @Test
    void shouldCreateAppointmentWithPendingStatus() {
        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setPreferredDateTime(LocalDateTime.of(2025, 6, 1, 14, 0));
        dto.setAppointmentType(AppointmentType.STATIONARY);
        dto.setDoctorId(1L);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setId(1L);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        UserEntity mockUser = new UserEntity();
        PatientEntity patient = new PatientEntity();
        mockUser.setPatient(patient);

        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(mockUser));

        appointmentService.createPendingAppointment(dto);

        ArgumentCaptor<AppointmentEntity> captor = ArgumentCaptor.forClass(AppointmentEntity.class);
        verify(appointmentRepository).save(captor.capture());

        AppointmentEntity saved = captor.getValue();
        assertThat(saved.getPatient()).isEqualTo(patient);
        assertThat(saved.getDoctor()).isEqualTo(doctor);
        assertThat(saved.getStatus()).isEqualTo(AppointmentStatus.PENDING);
    }

    @Test
    void shouldThrowIfUserIsNotPatient() {
        UserEntity mockUser = new UserEntity();
        mockUser.setPatient(null); // brak pacjenta

        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(mockUser));

        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setPreferredDateTime(LocalDateTime.now());
        dto.setAppointmentType(AppointmentType.STATIONARY);

        assertThatThrownBy(() -> appointmentService.createPendingAppointment(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Only patients can request appointments.");
    }

    @Test
    void shouldAssignDoctorAndConfirmAppointment() {
        AssignAppointmentDto dto = new AssignAppointmentDto();
        dto.setDoctorId(1L);
        dto.setAppointmentDate(LocalDateTime.of(2025, 6, 2, 12, 0));

        AppointmentEntity appointment = new AppointmentEntity();
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        DoctorEntity doctor = new DoctorEntity();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        appointmentService.assignDoctorAndConfirm(1L, dto);

        assertThat(appointment.getDoctor()).isEqualTo(doctor);
        assertThat(appointment.getAppointmentDate()).isEqualTo(dto.getAppointmentDate());
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }

    @Test
    void shouldCreateConfirmedAppointment() {
        AppointmentCreationDto dto = new AppointmentCreationDto();
        dto.setPatientId(10L);
        dto.setDoctorId(20L);
        dto.setAppointmentDate(LocalDateTime.of(2025, 6, 5, 10, 0));
        dto.setAppointmentType(AppointmentType.TELECONSULTATION);

        PatientEntity patient = new PatientEntity();
        UserEntity user = new UserEntity();
        user.setPatient(patient);

        DoctorEntity doctor = new DoctorEntity();

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(doctorRepository.findById(20L)).thenReturn(Optional.of(doctor));

        appointmentService.createConfirmedAppointment(dto);

        ArgumentCaptor<AppointmentEntity> captor = ArgumentCaptor.forClass(AppointmentEntity.class);
        verify(appointmentRepository).save(captor.capture());

        AppointmentEntity saved = captor.getValue();
        assertThat(saved.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
        assertThat(saved.getDoctor()).isEqualTo(doctor);
        assertThat(saved.getPatient()).isEqualTo(patient);
    }

    @Test
    void shouldReturnAppointmentsForDoctor() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("doctor@example.com");
        when(auth.isAuthenticated()).thenReturn(true);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        UserEntity user = new UserEntity();
        user.setEmail("doctor@example.com");
        DoctorEntity doctor = new DoctorEntity();
        doctor.setId(77L);
        user.setRole(Role.DOCTOR);
        user.setDoctor(doctor);

        AppointmentEntity appointment1 = new AppointmentEntity();
        appointment1.setDoctor(doctor);

        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(user));

        when(appointmentRepository.findAllByDoctor_User(user)).thenReturn(List.of(appointment1));

        // When
        var result = appointmentService.getAppointmentsForCurrentUser();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDoctor()).isEqualTo(doctor);
    }

    @Test
    void shouldThrowWhenUserRoleIsNotSupported() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin@example.com");
        when(auth.isAuthenticated()).thenReturn(true);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        UserEntity user = new UserEntity();
        user.setEmail("admin@example.com");
        user.setRole(Role.ADMIN);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        // Then
        assertThatThrownBy(() -> appointmentService.getAppointmentsForCurrentUser())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Role ADMIN not allowed");
    }


}
