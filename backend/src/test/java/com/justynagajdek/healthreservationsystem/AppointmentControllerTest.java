package com.justynagajdek.healthreservationsystem;

import com.justynagajdek.healthreservationsystem.controller.AppointmentController;
import com.justynagajdek.healthreservationsystem.dto.AppointmentDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.enums.AppointmentStatus;
import com.justynagajdek.healthreservationsystem.enums.AppointmentType;
import com.justynagajdek.healthreservationsystem.jwt.JwtAuthenticationFilter;
import com.justynagajdek.healthreservationsystem.mapper.AppointmentMapper;
import com.justynagajdek.healthreservationsystem.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AppointmentController.class,
        excludeFilters = @ComponentScan.Filter(
                type  = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private AppointmentMapper appointmentMapper;

    @Test
    @WithMockUser(roles = "PATIENT")
    void shouldReturnMyAppointments() throws Exception {
        // given
        AppointmentEntity entity = new AppointmentEntity();
        entity.setId(1L);
        entity.setAppointmentDate(LocalDateTime.of(2025, 6, 1, 10, 0));
        entity.setStatus(AppointmentStatus.PENDING);
        entity.setAppointmentType(AppointmentType.STATIONARY);

        AppointmentDto dto = new AppointmentDto();
        dto.setId(1L);
        dto.setAppointmentDate(entity.getAppointmentDate());
        dto.setStatus(entity.getStatus());
        dto.setAppointmentType(entity.getAppointmentType());
        dto.setDoctorName("Dr. Smith");
        dto.setPatientName("John Doe");

        when(appointmentService.getAppointmentsForCurrentUser())
                .thenReturn(List.of(entity));
        when(appointmentMapper.toDtoList(List.of(entity)))
                .thenReturn(List.of(dto));

        // when & then
        mockMvc.perform(get("/appointments/mine"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].doctorName").value("Dr. Smith"))
                .andExpect(jsonPath("$[0].patientName").value("John Doe"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].appointmentType").value("STATIONARY"));
    }
}
