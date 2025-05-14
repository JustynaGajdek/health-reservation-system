package com.justynagajdek.healthreservationsystem.repository;

import com.justynagajdek.healthreservationsystem.entity.NurseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<NurseEntity, Long> {
}
