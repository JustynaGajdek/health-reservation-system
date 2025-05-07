package com.justynagajdek.healthreservationsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justynagajdek.healthreservationsystem.dto.LoginDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

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
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnJwtTokenWhenCredentialsAreValid() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("john.doe@email.com");
        loginDto.setPassword("pas123");

        mockMvc.perform(post("/login")
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

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

}