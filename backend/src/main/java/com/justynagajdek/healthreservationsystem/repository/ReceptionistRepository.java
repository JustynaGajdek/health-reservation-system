package com.justynagajdek.healthreservationsystem.repository;

import com.justynagajdek.healthreservationsystem.entity.ReceptionistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionistRepository extends JpaRepository<ReceptionistEntity, Long> {
}
