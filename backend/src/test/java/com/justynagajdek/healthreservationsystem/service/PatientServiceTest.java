package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.exception.ResourceNotFoundException;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock private PatientRepository patientRepository;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerPatientShouldCreateUserAndPatient() {
        SignUpDto dto = new SignUpDto();
        dto.setEmail("john.doe@example.com");
        dto.setPassword("secret");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPhone("111222333");
        dto.setPesel("99010112345");
        dto.setDateOfBirth(LocalDate.of(1999, 1, 1));
        dto.setAddress("123 Main St");

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("ENC");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        PatientEntity savedPatient = new PatientEntity();
        savedPatient.setId(10L);
        when(patientRepository.save(any(PatientEntity.class))).thenReturn(savedPatient);

        PatientEntity result = patientService.registerPatient(dto);

        assertEquals(10L, result.getId());
        verify(userRepository).save(any(UserEntity.class));
        verify(patientRepository).save(any(PatientEntity.class));
    }

    @Test
    void getByPeselShouldReturnPatientWhenExists() {
        PatientEntity patient = new PatientEntity();
        patient.setPesel("12345678901");
        when(patientRepository.findByPesel("12345678901")).thenReturn(Optional.of(patient));

        PatientEntity result = patientService.getByPesel("12345678901");
        assertEquals("12345678901", result.getPesel());
    }

    @Test
    void getByPeselShouldThrowWhenNotFound() {
        when(patientRepository.findByPesel("00000000000")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getByPesel("00000000000"));
    }

    @Test
    void getMyProfileShouldReturnPatientProfile() {
        String email = "jane.doe@example.com";
        PatientEntity patient = new PatientEntity();
        patient.setUser(new UserEntity());
        when(patientRepository.findByUserEmail(email)).thenReturn(Optional.of(patient));

        PatientEntity result = patientService.getMyProfile(email);
        assertSame(patient, result);
    }

    @Test
    void getMyProfileShouldThrowWhenNotFound() {
        when(patientRepository.findByUserEmail("missing@example.com"))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getMyProfile("missing@example.com"));
    }

    @Test
    void updateProfileShouldModifyUserAndPatient() {
        String email = "joe.bloggs@example.com";
        UserEntity user = new UserEntity();
        user.setFirstName("Joe");
        user.setLastName("Bloggs");
        user.setPhoneNumber("000111222");

        PatientEntity existing = new PatientEntity();
        existing.setId(5L);
        existing.setUser(user);
        existing.setDateOfBirth(LocalDate.of(1980, 5, 20));
        existing.setAddress("Old Addr");

        when(patientRepository.findByUserEmail(email)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(patientRepository.save(any(PatientEntity.class))).thenAnswer(i -> i.getArgument(0));

        PatientDto dto = new PatientDto();
        dto.setFirstName("Joseph");
        dto.setLastName("Bloggsington");
        dto.setPhoneNumber("999888777");
        dto.setDateOfBirth(LocalDate.of(1981, 6, 21));
        dto.setAddress("New Addr");

        PatientEntity updated = patientService.updateProfile(email, dto);

        assertEquals("Joseph", updated.getUser().getFirstName());
        assertEquals("Bloggsington", updated.getUser().getLastName());
        assertEquals("999888777", updated.getUser().getPhoneNumber());
        assertEquals(LocalDate.of(1981, 6, 21), updated.getDateOfBirth());
        assertEquals("New Addr", updated.getAddress());

        verify(userRepository).save(user);
        verify(patientRepository).save(existing);
    }
}
