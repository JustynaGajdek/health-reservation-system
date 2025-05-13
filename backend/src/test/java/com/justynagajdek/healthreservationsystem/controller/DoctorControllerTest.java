package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.DoctorDto;
import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.jwt.JwtAuthenticationFilter;
import com.justynagajdek.healthreservationsystem.service.DoctorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = DoctorController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Test
    @WithMockUser(roles = "DOCTOR")
    @DisplayName("GET /doctor/profile should return doctor data")
    void shouldReturnDoctorProfile() throws Exception {
        DoctorDto dto = new DoctorDto();
        dto.setEmail("doc@example.com");
        dto.setFullName("Anna Nowak");

        when(doctorService.getCurrentDoctorProfile()).thenReturn(dto);

        mockMvc.perform(get("/doctor/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("doc@example.com"))
                .andExpect(jsonPath("$.fullName").value("Anna Nowak"));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    @DisplayName("GET /doctor/patients/{id} should return patient data")
    void shouldReturnPatientInfo() throws Exception {
        PatientDto dto = new PatientDto();
        dto.setPesel("12345678901");
        dto.setFirstName("Jan");
        dto.setLastName("Kowalski");

        when(doctorService.getPatientInfo(42L)).thenReturn(dto);

        mockMvc.perform(get("/doctor/patients/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel").value("12345678901"))
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"));

    }
}
