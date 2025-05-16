package com.justynagajdek.healthreservationsystem.repository;

import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findByDoctorIsNull();
    List<AppointmentEntity> findByPatient_Id(Long patientId);
    List<AppointmentEntity> findByDoctor_Id(Long doctorId);

    boolean existsByAppointmentDateAndDoctorId(LocalDateTime appointmentDate, Long doctorId);

    List<AppointmentEntity> findAllByPatient_User(UserEntity user);
    List<AppointmentEntity> findAllByDoctor_User(UserEntity user);




}
