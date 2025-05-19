package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.VaccinationDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import com.justynagajdek.healthreservationsystem.mapper.VaccinationMapper;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import com.justynagajdek.healthreservationsystem.repository.VaccinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VaccinationServiceTest {

    @Mock
    private VaccinationRepository vaccinationRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private VaccinationMapper vaccinationMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VaccinationService vaccinationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddVaccinationWhenPatientExists() {
        Long patientId = 1L;
        VaccinationDto dto = new VaccinationDto();
        dto.setVaccineName("Flu");
        dto.setVaccinationDate(LocalDate.now());
        dto.setMandatory(true);

        PatientEntity patient = new PatientEntity();
        patient.setId(patientId);

        VaccinationEntity entity = new VaccinationEntity();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(vaccinationMapper.toEntity(dto)).thenReturn(entity);

        vaccinationService.addVaccination(patientId, dto);

        verify(vaccinationRepository).save(entity);
        assertEquals(patient, entity.getPatient());
    }

    @Test
    void shouldThrowWhenPatientNotFound() {
        Long patientId = 42L;
        VaccinationDto dto = new VaccinationDto();

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> vaccinationService.addVaccination(patientId, dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Patient not found", ex.getReason());

    }

    @Test
    void shouldReturnVaccinationsForCurrentPatient() {
        String email = "patient@example.com";

        var authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        var securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        PatientEntity patient = new PatientEntity();
        patient.setId(42L);

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPatient(patient);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        VaccinationEntity v1 = new VaccinationEntity();
        VaccinationEntity v2 = new VaccinationEntity();
        when(vaccinationRepository.findByPatientId(42L)).thenReturn(List.of(v1, v2));

        when(vaccinationMapper.toDto(any())).thenReturn(new VaccinationDto());

        List<VaccinationDto> result = vaccinationService.getVaccinationsForCurrentPatient();

        assertEquals(2, result.size());
    }

}
