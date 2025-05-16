package com.justynagajdek.healthreservationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "patients")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "pesel", nullable = false, unique = true)
    private String pesel;

    @Column(name = "birth_date", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "guardian_id")
    private PatientEntity guardian;

    @OneToMany(mappedBy = "patient")
    private List<AppointmentEntity> appointments;

    public void setFirstName(String jan) {
    }

    public void setLastName(String kowalski) {
    }
}

