package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.VaccinationDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import com.justynagajdek.healthreservationsystem.mapper.VaccinationMapper;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.VaccinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
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

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> vaccinationService.addVaccination(patientId, dto));

        assertEquals("Patient not found", ex.getMessage());
        verify(vaccinationRepository, never()).save(any());
    }
}
