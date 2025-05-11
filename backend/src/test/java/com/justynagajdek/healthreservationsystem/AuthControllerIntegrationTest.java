package com.justynagajdek.healthreservationsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justynagajdek.healthreservationsystem.dto.LoginDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.AppointmentRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

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

    @AfterEach
    void cleanUp() {
        appointmentRepository.deleteAll();
        userRepository.findByEmail("john.doe@email.com").ifPresent(userRepository::delete);

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

}