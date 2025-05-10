package com.justynagajdek.healthreservationsystem.repository;

import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findByPesel(String pesel);
}
