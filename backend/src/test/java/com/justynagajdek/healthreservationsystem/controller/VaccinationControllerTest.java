package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.jwt.JwtAuthenticationFilter;
import com.justynagajdek.healthreservationsystem.service.VaccinationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = VaccinationController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class VaccinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VaccinationService vaccinationService;

    @Test
    @WithMockUser(roles = "NURSE")
    @DisplayName("POST /vaccinations/patient/{id} should return 201 when nurse adds vaccination")
    void shouldAllowNurseToAddVaccination() throws Exception {
        String body = """
            {
              "vaccineName": "COVID-19",
              "vaccinationDate": "2024-12-01",
              "mandatory": true
            }
            """;

        doNothing().when(vaccinationService).addVaccination(eq(1L), any());

        mockMvc.perform(post("/vaccinations/patient/1")
                        .contentType("application/json")
                        .content(body)
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());
    }
}
