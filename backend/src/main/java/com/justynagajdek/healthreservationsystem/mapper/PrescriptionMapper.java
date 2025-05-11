package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.PrescriptionDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.entity.PrescriptionEntity;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {
    public PrescriptionEntity toEntity(PrescriptionDto dto, AppointmentEntity appointment) {
        PrescriptionEntity entity = new PrescriptionEntity();
        entity.setAppointment(appointment);
        entity.setMedicineName(dto.getMedicineName());
        entity.setDosage(dto.getDosage());
        entity.setInstructions(dto.getInstructions());
        return entity;
    }

    public PrescriptionDto toDto(PrescriptionEntity entity) {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setId(entity.getId());
        dto.setMedicineName(entity.getMedicineName());
        dto.setDosage(entity.getDosage());
        dto.setInstructions(entity.getInstructions());
        dto.setIssuedAt(entity.getIssuedAt());

        if (entity.getAppointment() != null) {
            dto.setAppointmentId(entity.getAppointment().getId());
        }

        return dto;
    }

}
