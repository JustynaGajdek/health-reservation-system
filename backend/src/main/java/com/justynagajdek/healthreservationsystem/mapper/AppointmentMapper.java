package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.AppointmentDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    public AppointmentDto toDto(AppointmentEntity entity) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(entity.getId());
        dto.setAppointmentDate(entity.getAppointmentDate());
        dto.setAppointmentType(entity.getAppointmentType());
        dto.setStatus(entity.getStatus());

        if (entity.getDoctor() != null) {
            dto.setDoctorName(entity.getDoctor().getUser().getFirstName() + " " + entity.getDoctor().getUser().getLastName());
        }

        if (entity.getPatient() != null) {
            dto.setPatientName(entity.getPatient().getUser().getFirstName() + " " + entity.getPatient().getUser().getLastName());
        }

        return dto;
    }

    public List<AppointmentDto> toDtoList(List<AppointmentEntity> appointments) {
        return appointments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
