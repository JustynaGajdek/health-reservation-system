package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.AppointmentDto;
import com.justynagajdek.healthreservationsystem.dto.AppointmentRequestDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.mapper.AppointmentMapper;
import com.justynagajdek.healthreservationsystem.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(AppointmentService appointmentService, AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }
    @PostMapping("/request")
    public ResponseEntity<AppointmentDto> requestAppointment(@RequestBody AppointmentRequestDto dto) {
        AppointmentEntity saved = appointmentService.createPendingAppointment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentMapper.toDto(saved));
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    @GetMapping("/mine")
    public List<AppointmentDto> getMyAppointments() {
        List<AppointmentEntity> appointments = appointmentService.getAppointmentsForCurrentUser();
        return appointmentMapper.toDtoList(appointments);
    }

}
