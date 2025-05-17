package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.AppointmentDto;
import com.justynagajdek.healthreservationsystem.entity.*;
import com.justynagajdek.healthreservationsystem.enums.AppointmentStatus;
import com.justynagajdek.healthreservationsystem.enums.AppointmentType;
import com.justynagajdek.healthreservationsystem.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentMapperTest {

    private AppointmentMapper appointmentMapper;

    @BeforeEach
    void setUp() {
        appointmentMapper = new AppointmentMapper();
    }

    @Test
    void shouldMapAppointmentEntityToDtoCorrectly() {
        // given
        UserEntity doctorUser = new UserEntity();
        doctorUser.setFirstName("Gregory");
        doctorUser.setLastName("House");

        DoctorEntity doctor = new DoctorEntity();
        doctor.setUser(doctorUser);

        UserEntity patientUser = new UserEntity();
        patientUser.setFirstName("Lisa");
        patientUser.setLastName("Cuddy");

        PatientEntity patient = new PatientEntity();
        patient.setUser(patientUser);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(1L);
        appointment.setAppointmentDate(LocalDateTime.of(2025, 5, 11, 10, 30));
        appointment.setAppointmentType(AppointmentType.TELECONSULTATION);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        // when
        AppointmentDto dto = appointmentMapper.toDto(appointment);

        // then
        assertEquals(1L, dto.getId());
        assertEquals(AppointmentType.TELECONSULTATION, dto.getAppointmentType());
        assertEquals(AppointmentStatus.CONFIRMED, dto.getStatus());
        assertEquals("Gregory House", dto.getDoctorName());
        assertEquals("Lisa Cuddy", dto.getPatientName());
        assertEquals(LocalDateTime.of(2025, 5, 11, 10, 30), dto.getAppointmentDate());
    }

    @Test
    void shouldMapListOfAppointmentsToDtoList() {
        AppointmentEntity a1 = new AppointmentEntity();
        a1.setId(1L);
        a1.setAppointmentType(AppointmentType.STATIONARY);
        a1.setStatus(AppointmentStatus.PENDING);
        a1.setAppointmentDate(LocalDateTime.now());

        AppointmentEntity a2 = new AppointmentEntity();
        a2.setId(2L);
        a2.setAppointmentType(AppointmentType.TELECONSULTATION);
        a2.setStatus(AppointmentStatus.CANCEL_REQUESTED);
        a2.setAppointmentDate(LocalDateTime.now().plusDays(1));

        List<AppointmentDto> dtos = appointmentMapper.toDtoList(List.of(a1, a2));

        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(2L, dtos.get(1).getId());
    }
}
