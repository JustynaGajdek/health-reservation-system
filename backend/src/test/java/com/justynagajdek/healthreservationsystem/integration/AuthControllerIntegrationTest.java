package com.justynagajdek.healthreservationsystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justynagajdek.healthreservationsystem.dto.LoginDto;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.AppointmentRepository;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setUpUser() {
        userRepository.findByEmail("john.doe@email.com").ifPresent(userRepository::delete);

        UserEntity user = new UserEntity();
        user.setEmail("john.doe@email.com");
        user.setPasswordHash(passwordEncoder.encode("pas123"));
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.PATIENT);
        userRepository.save(user);
    }


    @Test
    void shouldReturnJwtTokenWhenCredentialsAreValid() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("john.doe@email.com");
        loginDto.setPassword("pas123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("john.doe@email.com");
        loginDto.setPassword("wrongPassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRegisterNewUser_WhenSignUpDtoIsValid() throws Exception {
        SignUpDto dto = new SignUpDto();
        dto.setEmail("alice@example.com");
        dto.setPassword("securePass1!");
        dto.setFirstName("Alice");
        dto.setLastName("Smith");
        dto.setRole(String.valueOf(Role.PATIENT));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.id").isNumber());

        assertTrue(userRepository.findByEmail("alice@example.com").isPresent());
    }

}