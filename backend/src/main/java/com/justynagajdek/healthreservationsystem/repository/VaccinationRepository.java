package com.justynagajdek.healthreservationsystem.repository;

import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccinationRepository extends JpaRepository<VaccinationEntity, Long> {
    List<VaccinationEntity> findByPatientId(Long patientId);
}
