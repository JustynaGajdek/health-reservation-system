package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import com.justynagajdek.healthreservationsystem.entity.ReceptionistEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.DoctorRepository;
import com.justynagajdek.healthreservationsystem.repository.NurseRepository;
import com.justynagajdek.healthreservationsystem.repository.ReceptionistRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ReceptionistRepository receptionistRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NurseRepository nurseRepository;

    @InjectMocks
    private StaffService staffService;

    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;

    @Captor
    private ArgumentCaptor<DoctorEntity> doctorCaptor;

    @Captor
    private ArgumentCaptor<ReceptionistEntity> receptionistCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private SignUpDto createSampleDto(Role role) {
        SignUpDto dto = new SignUpDto();
        dto.setEmail("john@example.com");
        dto.setPassword("password123");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPhone("123456789");
        dto.setRole(role.name());
        return dto;
    }

    @Test
    void shouldRegisterDoctorSuccessfully() {
        SignUpDto dto = createSampleDto(Role.DOCTOR);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded123");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        staffService.registerDoctor(dto);

        verify(userRepository).save(userCaptor.capture());
        verify(doctorRepository).save(doctorCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        DoctorEntity savedDoctor = doctorCaptor.getValue();

        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("encoded123", savedUser.getPasswordHash());
        assertEquals(Role.DOCTOR, savedUser.getRole());
        assertEquals(savedUser, savedDoctor.getUser());
    }

    @Test
    void shouldRegisterReceptionistSuccessfully() {
        SignUpDto dto = createSampleDto(Role.RECEPTIONIST);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded456");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        staffService.registerReceptionist(dto);

        verify(userRepository).save(userCaptor.capture());
        verify(receptionistRepository).save(receptionistCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        ReceptionistEntity savedReceptionist = receptionistCaptor.getValue();

        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("encoded456", savedUser.getPasswordHash());
        assertEquals(Role.RECEPTIONIST, savedUser.getRole());
        assertEquals(savedUser, savedReceptionist.getUser());
    }

    @Test
    void shouldThrowExceptionIfEmailExists() {
        SignUpDto dto = createSampleDto(Role.DOCTOR);
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new UserEntity()));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> staffService.registerDoctor(dto)
        );

        assertEquals("Email already in use", ex.getMessage());
        verify(userRepository, never()).save(any());
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void shouldRegisterNurseSuccessfully() {
        SignUpDto dto = new SignUpDto();
        dto.setEmail("nurse@example.com");
        dto.setPassword("nurse123");
        dto.setFirstName("Anna");
        dto.setLastName("Nowak");
        dto.setPhone("123456789");
        dto.setRole("NURSE");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        staffService.registerNurse(dto);

        verify(userRepository).save(any());
        verify(nurseRepository).save(any());
    }

}
