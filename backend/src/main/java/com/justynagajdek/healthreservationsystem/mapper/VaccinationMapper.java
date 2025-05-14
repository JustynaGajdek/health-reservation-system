package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.VaccinationDto;
import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import org.springframework.stereotype.Component;

@Component
public class VaccinationMapper {

    public VaccinationDto toDto(VaccinationEntity entity) {
        if (entity == null) {
            return null;
        }

        VaccinationDto dto = new VaccinationDto();
        dto.setId(entity.getId());
        dto.setVaccineName(entity.getVaccineName());
        dto.setVaccinationDate(entity.getVaccinationDate());
        dto.setMandatory(entity.isMandatory());
        return dto;
    }

    public VaccinationEntity toEntity(VaccinationDto dto) {
        if (dto == null) {
            return null;
        }

        VaccinationEntity entity = new VaccinationEntity();
        entity.setId(dto.getId());
        entity.setVaccineName(dto.getVaccineName());
        entity.setVaccinationDate(dto.getVaccinationDate());
        entity.setMandatory(dto.isMandatory());
        return entity;
    }
}
