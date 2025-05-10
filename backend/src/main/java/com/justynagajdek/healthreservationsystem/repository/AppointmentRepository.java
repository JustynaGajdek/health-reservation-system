package com.justynagajdek.healthreservationsystem.repository;

import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findByDoctorIsNull();
    List<AppointmentEntity> findByPatient_Id(Long patientId);
    List<AppointmentEntity> findByDoctor_Id(Long doctorId);


}
