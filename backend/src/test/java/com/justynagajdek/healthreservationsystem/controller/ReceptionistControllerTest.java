package com.justynagajdek.healthreservationsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justynagajdek.healthreservationsystem.dto.AssignAppointmentDto;
import com.justynagajdek.healthreservationsystem.dto.AppointmentCreationDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.jwt.JwtAuthenticationFilter;
import com.justynagajdek.healthreservationsystem.mapper.PatientMapper;
import com.justynagajdek.healthreservationsystem.service.AppointmentService;
import com.justynagajdek.healthreservationsystem.service.PatientService;
import com.justynagajdek.healthreservationsystem.service.PrescriptionService;
import com.justynagajdek.healthreservationsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ReceptionistController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        ),
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class ReceptionistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private PrescriptionService prescriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private PatientService patientService;
    @MockBean
    private  PatientMapper patientMapper;


    @Test
    void shouldReturnUnassignedAppointmentsList() throws Exception {
        AppointmentEntity entity = new AppointmentEntity();
        entity.setId(42L);
        when(appointmentService.getUnassignedAppointments())
                .thenReturn(List.of(entity));

        mockMvc.perform(get("/reception/appointments/unassigned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(42L));
    }

    @Test
    void shouldAssignAppointmentAndConfirm() throws Exception {
        AssignAppointmentDto dto = new AssignAppointmentDto();
        doNothing().when(appointmentService).assignDoctorAndConfirm(anyLong(), any(AssignAppointmentDto.class));

        mockMvc.perform(put("/reception/appointments/{id}/assign", 7)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment assigned and confirmed."));
    }

    @Test
    void shouldCreateAndConfirmAppointment() throws Exception {
        AppointmentCreationDto dto = new AppointmentCreationDto();
        doNothing().when(appointmentService).createConfirmedAppointment(any(AppointmentCreationDto.class));

        mockMvc.perform(post("/reception/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment created and confirmed."));
    }
}
