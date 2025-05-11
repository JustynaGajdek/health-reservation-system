package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserMapper.class})
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapSignUpDtoToUserEntityCorrectly() {
        // given
        SignUpDto dto = new SignUpDto();
        dto.setFirstName("Tomek");
        dto.setLastName("Kowalski");
        dto.setEmail("tomek@wp.pl");
        dto.setPhone("123123123");
        dto.setPassword("secret123");
        dto.setRole("RECEPTIONIST");

        // when
        UserEntity entity = userMapper.mapSignUpDtoToUserEntity(dto);

        // then
        assertEquals("Tomek", entity.getFirstName());
        assertEquals("Kowalski", entity.getLastName());
        assertEquals("tomek@wp.pl", entity.getEmail());
        assertEquals("123123123", entity.getPhoneNumber());
        assertEquals(Role.RECEPTIONIST, entity.getRole());
        assertEquals(AccountStatus.PENDING, entity.getStatus());
    }

    @Test
    void shouldMapUserEntityToUserDto() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setFirstName("Ala");
        entity.setLastName("Nowak");
        entity.setEmail("ala@example.com");
        entity.setPhoneNumber("555111222");
        entity.setRole(Role.PATIENT);
        entity.setStatus(AccountStatus.ACTIVE);

        var dto = userMapper.toDto(entity);

        assertEquals(1L, dto.getId());
        assertEquals("Ala", dto.getFirstName());
        assertEquals("Nowak", dto.getLastName());
        assertEquals("ala@example.com", dto.getEmail());
        assertEquals("555111222", dto.getPhone());
        assertEquals(Role.PATIENT, dto.getRole());
        assertEquals(AccountStatus.ACTIVE, dto.getStatus());
    }

    @Test
    void shouldMapUserEntityToSignUpDto() {
        UserEntity entity = new UserEntity();
        entity.setFirstName("Basia");
        entity.setLastName("Lis");
        entity.setEmail("basia@example.com");
        entity.setPhoneNumber("999888777");
        entity.setRole(Role.RECEPTIONIST);

        var dto = userMapper.mapToSignUpDto(entity);

        assertEquals("Basia", dto.getFirstName());
        assertEquals("Lis", dto.getLastName());
        assertEquals("basia@example.com", dto.getEmail());
        assertEquals("999888777", dto.getPhone());
        assertEquals("RECEPTIONIST", dto.getRole());
    }

}
