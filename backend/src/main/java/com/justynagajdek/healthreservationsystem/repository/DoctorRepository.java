package com.justynagajdek.healthreservationsystem.repository;

import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
}