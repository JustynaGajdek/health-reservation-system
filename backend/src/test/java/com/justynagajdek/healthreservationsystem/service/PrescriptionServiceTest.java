package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.*;
import com.justynagajdek.healthreservationsystem.mapper.PrescriptionMapper;
import com.justynagajdek.healthreservationsystem.repository.AppointmentRepository;
import com.justynagajdek.healthreservationsystem.repository.PrescriptionRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrescriptionMapper prescriptionMapper;

    @InjectMocks
    private PrescriptionService prescriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePrescriptionSuccessfully() {
        // Arrange
        PrescriptionDto dto = new PrescriptionDto();
        dto.setAppointmentId(1L);
        dto.setMedicineName("Ibuprofen");

        AppointmentEntity appointment = new AppointmentEntity();
        PrescriptionEntity prescriptionEntity = new PrescriptionEntity();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(prescriptionMapper.toEntity(dto, appointment)).thenReturn(prescriptionEntity);
        when(prescriptionRepository.save(prescriptionEntity)).thenReturn(prescriptionEntity);

        when(prescriptionMapper.toDto(any())).thenAnswer(inv -> {
            System.out.println("✅ Mocked mapper.toDto was called");
            PrescriptionDto result = new PrescriptionDto();
            result.setMedicineName("Ibuprofen");
            return result;
        });

        // Act
        PrescriptionDto result = prescriptionService.createPrescription(dto);

        // Assert
        System.out.println("Returned DTO medicine: " + result.getMedicineName());
        assertThat(result.getMedicineName()).isEqualTo("Ibuprofen");
    }

    @Test
    void shouldThrowWhenAppointmentNotFoundDuringCreation() {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setAppointmentId(404L);

        when(appointmentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> prescriptionService.createPrescription(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Appointment not found");
    }

    @Test
    void shouldReturnPrescriptionsForAppointment() {
        PrescriptionEntity p1 = new PrescriptionEntity();
        PrescriptionDto d1 = new PrescriptionDto();

        when(prescriptionRepository.findByAppointmentId(100L)).thenReturn(List.of(p1));
        when(prescriptionMapper.toDto(p1)).thenReturn(d1);

        var result = prescriptionService.getPrescriptionsForAppointment(100L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnPrescriptionsForCurrentPatient() {
        String email = "patient@example.com";
        UserEntity user = new UserEntity();
        PatientEntity patient = new PatientEntity();
        user.setPatient(patient);

        PrescriptionEntity p1 = new PrescriptionEntity();
        PrescriptionDto dto = new PrescriptionDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(prescriptionRepository.findByAppointmentPatient(patient)).thenReturn(List.of(p1));
        when(prescriptionMapper.toDto(p1)).thenReturn(dto);

        var result = prescriptionService.getPrescriptionsForCurrentPatient(email);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldThrowWhenUserNotFoundForEmail() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                prescriptionService.getPrescriptionsForCurrentPatient("notfound@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldReturnPrescriptionsByPatientId() {
        UserEntity user = new UserEntity();
        PatientEntity patient = new PatientEntity();
        user.setPatient(patient);

        PrescriptionEntity p1 = new PrescriptionEntity();
        PrescriptionDto dto = new PrescriptionDto();

        when(userRepository.findById(123L)).thenReturn(Optional.of(user));
        when(prescriptionRepository.findByAppointmentPatient(patient)).thenReturn(List.of(p1));
        when(prescriptionMapper.toDto(p1)).thenReturn(dto);

        var result = prescriptionService.getPrescriptionsByPatientId(123L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldThrowWhenUserHasNoPatientProfile() {
        UserEntity user = new UserEntity();

        when(userRepository.findById(123L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> prescriptionService.getPrescriptionsByPatientId(123L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Patient not found");
    }
}
