package com.justynagajdek.healthreservationsystem;

import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
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
}
