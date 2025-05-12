package com.justynagajdek.healthreservationsystem.mapper;

import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientDto toDto(PatientEntity e) {
        if (e == null) return null;
        PatientDto dto = new PatientDto();
        dto.setId(e.getId());
        dto.setEmail(e.getUser().getEmail());
        dto.setFirstName(e.getUser().getFirstName());
        dto.setLastName(e.getUser().getLastName());
        dto.setPhoneNumber(e.getUser().getPhoneNumber());
        dto.setPesel(e.getPesel());
        dto.setDateOfBirth(e.getDateOfBirth());
        dto.setAddress(e.getAddress());
        return dto;
    }

    public PatientEntity toEntity(PatientDto dto) {
        if (dto == null) return null;
        PatientEntity e = new PatientEntity();
        e.setPesel(dto.getPesel());
        e.setDateOfBirth(dto.getDateOfBirth());
        e.setAddress(dto.getAddress());
        return e;
    }
}
