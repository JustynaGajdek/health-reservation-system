package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.UserDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.mapper.UserMapper;
import com.justynagajdek.healthreservationsystem.service.UserService;
import com.justynagajdek.healthreservationsystem.jwt.JwtAuthenticationFilter;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AdminController.class,
        excludeFilters = @ComponentScan.Filter(
                type  = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin/users should return all users")
    void shouldReturnAllUsers() throws Exception {
        var entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("admin@example.com");

        var dto = new UserDto();
        dto.setEmail("admin@example.com");

        when(userService.getAllUsers()).thenReturn(List.of(entity));
        when(userMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("admin@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin/users/pending should return pending users")
    void shouldReturnPendingUsers() throws Exception {
        var pending = new UserEntity();
        pending.setId(2L);
        pending.setEmail("pending@example.com");
        pending.setStatus(AccountStatus.PENDING);

        var dto = new UserDto();
        dto.setEmail("pending@example.com");

        when(userService.getUsersByStatus(AccountStatus.PENDING)).thenReturn(List.of(pending));
        when(userMapper.toDto(pending)).thenReturn(dto);

        mockMvc.perform(get("/admin/users/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("pending@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /admin/users/{id} should return 204 No Content")
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/admin/users/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /admin/users/{id}/approve should return 204 No Content")
    void shouldApproveUser() throws Exception {
        doNothing().when(userService).approveUser(1L);

        mockMvc.perform(put("/admin/users/1/approve").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin/dashboard should return static string")
    void shouldReturnDashboardMessage() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Admin Dashboard - Users, Doctors, Patients, Receptionists"
                ));
    }
}
