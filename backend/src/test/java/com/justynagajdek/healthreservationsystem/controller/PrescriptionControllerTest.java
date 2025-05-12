package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.PrescriptionEntity;
import com.justynagajdek.healthreservationsystem.jwt.JwtAuthenticationFilter;
import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import com.justynagajdek.healthreservationsystem.mapper.PrescriptionMapper;
import com.justynagajdek.healthreservationsystem.service.PrescriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PrescriptionController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrescriptionService prescriptionService;

    @MockBean
    private PrescriptionMapper prescriptionMapper;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = "DOCTOR")
    @DisplayName("POST /prescriptions should create prescription")
    void shouldCreatePrescription() throws Exception {
        var savedDto = new PrescriptionDto();
        savedDto.setId(1L);

        when(prescriptionService.createPrescription(any(PrescriptionDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/prescriptions")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new PrescriptionDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    @DisplayName("GET /prescriptions/by-appointment returns list")
    void shouldGetByAppointment() throws Exception {
        var entity = new PrescriptionEntity();
        var dto = new PrescriptionDto();

        when(prescriptionService.getPrescriptionsForAppointment(eq(5L))).thenReturn(List.of(entity));
        when(prescriptionMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(get("/prescriptions/by-appointment")
                        .param("appointmentId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = "PATIENT", username = "patient@example.com")
    @DisplayName("GET /prescriptions/my returns current patient prescriptions")
    void shouldGetPrescriptionsForCurrentPatient() throws Exception {
        var dto = new PrescriptionDto();
        dto.setId(10L);

        when(jwtTokenUtil.getUsernameFromJwtToken(anyString())).thenReturn("patient@example.com");
        when(prescriptionService.getPrescriptionsForCurrentPatient(eq("patient@example.com")))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/prescriptions/my")
                        .header("Authorization", "Bearer dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(10L));
    }

    @Test
    @WithMockUser(roles = "RECEPTIONIST")
    @DisplayName("GET /prescriptions/by-patient returns prescriptions by patient ID")
    void shouldGetPrescriptionsByPatientId() throws Exception {
        var dto = new PrescriptionDto();
        dto.setId(20L);

        when(prescriptionService.getPrescriptionsByPatientId(eq(3L))).thenReturn(List.of(dto));

        mockMvc.perform(get("/prescriptions/by-patient")
                        .param("patientId", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(20L));
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    @DisplayName("GET /prescriptions/by-patient allowed for PATIENT and returns empty list")
    void shouldAllowByPatientForPatientRole() throws Exception {
        when(prescriptionService.getPrescriptionsByPatientId(eq(2L))).thenReturn(List.of());

        mockMvc.perform(get("/prescriptions/by-patient")
                        .param("patientId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "RECEPTIONIST")
    @DisplayName("GET /prescriptions/by-patient returns 400 for missing param")
    void shouldRejectByPatientMissingParam() throws Exception {
        mockMvc.perform(get("/prescriptions/by-patient"))
                .andExpect(status().isBadRequest());
    }
}
