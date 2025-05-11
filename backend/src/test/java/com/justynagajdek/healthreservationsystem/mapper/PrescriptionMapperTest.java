package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.entity.PrescriptionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PrescriptionMapperTest {

    private PrescriptionMapper prescriptionMapper;

    @BeforeEach
    void setUp() {
        prescriptionMapper = new PrescriptionMapper();
    }

    @Test
    void shouldConvertEntityToDtoCorrectly() {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(42L);

        PrescriptionEntity entity = new PrescriptionEntity();
        entity.setId(1L);
        entity.setMedicineName("Ibuprofen");
        entity.setDosage("2 times a day");
        entity.setInstructions("Take after meals");
        entity.setIssuedAt(LocalDateTime.of(2025, 5, 11, 12, 0));
        entity.setAppointment(appointment);

        PrescriptionDto dto = prescriptionMapper.toDto(entity);

        assertEquals(1L, dto.getId(), "ID should match");
        assertEquals("Ibuprofen", dto.getMedicineName(), "Medicine name should be preserved");
        assertEquals("2 times a day", dto.getDosage(), "Dosage should be preserved");
        assertEquals("Take after meals", dto.getInstructions(), "Instructions should be preserved");
        assertEquals(LocalDateTime.of(2025, 5, 11, 12, 0), dto.getIssuedAt(), "Issued date should match");
        assertEquals(42L, dto.getAppointmentId(), "Appointment ID should be mapped");
    }

    @Test
    void shouldConvertDtoToEntityWithAppointmentAttached() {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setMedicineName("Paracetamol");
        dto.setDosage("3 times a day");
        dto.setInstructions("Drink with a full glass of water");

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(99L);

        PrescriptionEntity entity = prescriptionMapper.toEntity(dto, appointment);

        assertEquals("Paracetamol", entity.getMedicineName(), "Medicine should match");
        assertEquals("3 times a day", entity.getDosage(), "Dosage should match");
        assertEquals("Drink with a full glass of water", entity.getInstructions(), "Instructions should be carried over");
        assertEquals(appointment, entity.getAppointment(), "Appointment should be attached");
    }
}
