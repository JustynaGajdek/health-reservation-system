package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.DoctorDto;
import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorDto toDto(DoctorEntity entity) {
        if (entity == null || entity.getUser() == null) {
            return null;
        }

        DoctorDto dto = new DoctorDto();
        dto.setId(entity.getId());
        dto.setFullName(entity.getUser().getFirstName() + " " + entity.getUser().getLastName());
        dto.setEmail(entity.getUser().getEmail());
        dto.setSpecialization(entity.getSpecialization());
        dto.setOfficeNumber(entity.getOfficeNumber());
        dto.setWorkingHours(entity.getWorkingHours());
        return dto;
    }
}
