package com.justynagajdek.healthreservationsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justynagajdek.healthreservationsystem.dto.LoginDto;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import com.justynagajdek.healthreservationsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerUnitTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthenticationManager authManager;
    @MockBean
    JwtTokenUtil jwtTokenUtil;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    void loginSuccessShouldReturnToken() throws Exception {
        var req = new LoginDto("a@b.pl","pwd");
        when(authManager.authenticate(any()))
                .thenReturn(mock(Authentication.class));
        when(jwtTokenUtil.generateJwtToken("a@b.pl"))
                .thenReturn("JWT123");

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("JWT123"));
    }

    @Test
    void loginFailureShouldReturn401() throws Exception {
        var req = new LoginDto("x@y.pl","bad");
        when(authManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad"));

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerSuccessShouldReturn201() throws Exception {
        SignUpDto dto = new SignUpDto();
        dto.setFirstName("Anna");
        dto.setLastName("Kowalska");
        dto.setEmail("anna.kowalska@example.com");
        dto.setPhone("123456789");
        dto.setPassword("securePass123");
        dto.setRole("PATIENT");

        when(userService.registerNewUser(dto)).thenReturn(new UserEntity());

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Account created")));
    }

    @Test
    @WithMockUser
    void registerBadDataShouldReturn400() throws Exception {
        SignUpDto badDto = new SignUpDto();
        badDto.setEmail("exists@example.com");
        when(userService.registerNewUser(any(SignUpDto.class)))
                .thenThrow(new IllegalArgumentException("Email already in use"));

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email already in use")));
    }

}