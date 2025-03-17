package com.justynagajdek.healthreservationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "doctors")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String specialization;

    @Column(name = "office_number")
    private String officeNumber;

    @Column(name = "working_hours", columnDefinition = "jsonb")
    private String workingHours;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToMany(mappedBy = "doctor")
    private List<AppointmentEntity> appointments;

}
