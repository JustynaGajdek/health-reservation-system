package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PatientMapperTest {

    private PatientMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PatientMapper();
    }

    @Test
    void toDto_nullInput_returnsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void toEntity_nullInput_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDto_withEntity_returnsCorrectDto() {
        // Given
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setPhoneNumber("123456789");

        PatientEntity entity = new PatientEntity();
        entity.setId(1L);
        entity.setUser(user);
        entity.setPesel("89010112345");
        entity.setDateOfBirth(LocalDate.of(1989, 1, 1));
        entity.setAddress("Some Address");

        // When
        PatientDto dto = mapper.toDto(entity);

        // Then
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("Jan", dto.getFirstName());
        assertEquals("Kowalski", dto.getLastName());
        assertEquals("123456789", dto.getPhoneNumber());
        assertEquals("89010112345", dto.getPesel());
        assertEquals(LocalDate.of(1989, 1, 1), dto.getDateOfBirth());
        assertEquals("Some Address", dto.getAddress());
    }

    @Test
    void toEntity_withDto_returnsCorrectEntity() {
        // Given
        PatientDto dto = new PatientDto();
        dto.setPesel("89010112345");
        dto.setDateOfBirth(LocalDate.of(1989, 1, 1));
        dto.setAddress("Some Address");

        // When
        PatientEntity entity = mapper.toEntity(dto);

        // Then
        assertNotNull(entity);
        assertNull(entity.getId()); // id not set by mapper
        assertEquals("89010112345", entity.getPesel());
        assertEquals(LocalDate.of(1989, 1, 1), entity.getDateOfBirth());
        assertEquals("Some Address", entity.getAddress());
        assertNull(entity.getUser()); // user not set by mapper
    }
}
