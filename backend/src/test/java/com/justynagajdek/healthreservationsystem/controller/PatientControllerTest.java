package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.dto.UserDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.mapper.PatientMapper;
import com.justynagajdek.healthreservationsystem.mapper.UserMapper;
import com.justynagajdek.healthreservationsystem.service.PatientService;
import com.justynagajdek.healthreservationsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @Mock
    private PatientService patientService;
    @Mock
    private UserService userService;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private Authentication auth;

    private PatientController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PatientController(patientService, userService, patientMapper, userMapper);
    }

    @Test
    void shouldReturnUserDtoWhenGetProfile() {
        String email = "test@example.com";
        UserEntity entity = new UserEntity();
        UserDto dto = new UserDto();

        when(auth.getName()).thenReturn(email);
        when(userService.findByEmail(email)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<UserDto> response = controller.getProfile(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dto, response.getBody());
        verify(userService).findByEmail(email);
        verify(userMapper).toDto(entity);
    }

    @Test
    void shouldPropagateExceptionWhenUserServiceThrowsInGetProfile() {
        String email = "error@example.com";
        when(auth.getName()).thenReturn(email);
        when(userService.findByEmail(email)).thenThrow(new RuntimeException("Database down"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> controller.getProfile(auth));
        assertEquals("Database down", exception.getMessage());
        verify(userService).findByEmail(email);
    }

    @Test
    void shouldReturnUpdatedUserDtoWhenUpdateProfile() {
        String email = "user@ex.com";
        UserDto inputDto = new UserDto();
        UserEntity updatedEntity = new UserEntity();
        UserDto returnedDto = new UserDto();

        when(auth.getName()).thenReturn(email);
        when(userService.updateProfile(email, inputDto)).thenReturn(updatedEntity);
        when(userMapper.toDto(updatedEntity)).thenReturn(returnedDto);

        ResponseEntity<UserDto> response = controller.updateProfile(auth, inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(returnedDto, response.getBody());
        verify(userService).updateProfile(email, inputDto);
        verify(userMapper).toDto(updatedEntity);
    }

    @Test
    void shouldReturnPatientDtoWhenGetByPesel() {
        String pesel = "12345678901";
        PatientEntity entity = new PatientEntity();
        PatientDto dto = new PatientDto();

        when(patientService.getByPesel(pesel)).thenReturn(entity);
        when(patientMapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<PatientDto> response = controller.getByPesel(pesel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dto, response.getBody());
        verify(patientService).getByPesel(pesel);
        verify(patientMapper).toDto(entity);
    }

    @Test
    void shouldReturnNotFoundWhenNoPatient() {
        String pesel = "00000000000";
        when(patientService.getByPesel(pesel)).thenReturn(null);

        ResponseEntity<PatientDto> response = controller.getByPesel(pesel);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientService).getByPesel(pesel);
        verify(patientMapper, never()).toDto(any());
    }
}
