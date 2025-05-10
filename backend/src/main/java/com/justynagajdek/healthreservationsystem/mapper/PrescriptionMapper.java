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
}
