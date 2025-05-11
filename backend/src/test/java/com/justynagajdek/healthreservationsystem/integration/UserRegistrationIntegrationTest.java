package com.justynagajdek.healthreservationsystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUpBefore() {
        userRepository.findByEmail("janedoe@mail.com").ifPresent(userRepository::delete);
    }


    @Test
    void shouldRegisterNewUserSuccessfully() throws Exception {
        // given
        SignUpDto dto = new SignUpDto();
        dto.setEmail("janedoe@mail.com");
        dto.setPassword("sdf123");
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setPhone("123456789");
        dto.setRole("PATIENT");

        // when + then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Account created. Waiting for admin approval."));

        UserEntity user = userRepository.findByEmail("janedoe@mail.com").orElse(null);
        assertNotNull(user);
        assertEquals(Role.PATIENT, user.getRole());
        assertEquals(AccountStatus.PENDING, user.getStatus());
    }
}
