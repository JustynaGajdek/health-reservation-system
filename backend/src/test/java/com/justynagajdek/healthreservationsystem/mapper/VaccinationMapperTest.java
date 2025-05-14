package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.VaccinationDto;
import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VaccinationMapperTest {

    private final VaccinationMapper mapper = new VaccinationMapper();

    @Test
    void shouldMapEntityToDto() {
        VaccinationEntity entity = new VaccinationEntity();
        entity.setId(1L);
        entity.setVaccineName("COVID-19");
        entity.setVaccinationDate(LocalDate.of(2023, 12, 1));
        entity.setMandatory(true);

        VaccinationDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("COVID-19", dto.getVaccineName());
        assertEquals(LocalDate.of(2023, 12, 1), dto.getVaccinationDate());
        assertTrue(dto.isMandatory());
    }

    @Test
    void shouldMapDtoToEntity() {
        VaccinationDto dto = new VaccinationDto();
        dto.setId(2L);
        dto.setVaccineName("Tetanus");
        dto.setVaccinationDate(LocalDate.of(2022, 5, 10));
        dto.setMandatory(false);

        VaccinationEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Tetanus", entity.getVaccineName());
        assertEquals(LocalDate.of(2022, 5, 10), entity.getVaccinationDate());
        assertFalse(entity.isMandatory());
    }

    @Test
    void shouldReturnNullForNullEntity() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void shouldReturnNullForNullDto() {
        assertNull(mapper.toEntity(null));
    }
}
