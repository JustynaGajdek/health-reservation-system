package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.AppointmentDto;
import com.justynagajdek.healthreservationsystem.dto.DoctorDto;
import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.entity.*;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.mapper.AppointmentMapper;
import com.justynagajdek.healthreservationsystem.mapper.DoctorMapper;
import com.justynagajdek.healthreservationsystem.mapper.PatientMapper;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private DoctorMapper doctorMapper;
    @Mock private AppointmentMapper appointmentMapper;
    @Mock private PatientMapper patientMapper;
    @InjectMocks private DoctorService doctorService;

    private DoctorEntity doctor;
    private UserEntity user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("doctor@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        user = new UserEntity();
        user.setEmail("doctor@example.com");
        user.setRole(Role.DOCTOR);

        doctor = new DoctorEntity();
        doctor.setId(1L);
        doctor.setUser(user);
        user.setDoctor(doctor);
    }

    @Test
    void shouldReturnDoctorProfile() {
        DoctorDto dto = new DoctorDto();
        dto.setEmail("doctor@example.com");

        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(user));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        DoctorDto result = doctorService.getCurrentDoctorProfile();

        assertEquals("doctor@example.com", result.getEmail());
    }

    @Test
    void shouldReturnAppointmentsForDoctor() {
        AppointmentEntity a1 = new AppointmentEntity();
        AppointmentEntity a2 = new AppointmentEntity();
        doctor.setAppointments(List.of(a1, a2));

        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(user));
        when(appointmentMapper.toDto(any())).thenReturn(new AppointmentDto());

        List<AppointmentDto> result = doctorService.getAppointmentsForCurrentDoctor();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnPatientInfoIfAssignedToDoctor() {
        PatientEntity patient = new PatientEntity();
        patient.setId(42L);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setPatient(patient);
        doctor.setAppointments(List.of(appointment));

        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(user));
        when(patientMapper.toDto(patient)).thenReturn(new PatientDto());

        PatientDto result = doctorService.getPatientInfo(42L);

        assertNotNull(result);
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> doctorService.getCurrentDoctorProfile());

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void shouldThrowIfUserIsNotDoctor() {
        user.setDoctor(null);
        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(user));

        Exception ex = assertThrows(IllegalStateException.class,
                () -> doctorService.getCurrentDoctorProfile());

        assertEquals("Current user is not a doctor", ex.getMessage());
    }

    @Test
    void shouldThrowIfPatientNotAssigned() {
        doctor.setAppointments(List.of());
        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(user));

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> doctorService.getPatientInfo(99L));

        assertEquals("Patient not found or not assigned to you", ex.getMessage());
    }
}
