package com.justynagajdek.healthreservationsystem.entity;

import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.PENDING;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PatientEntity patient;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private DoctorEntity doctor;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ReceptionistEntity receptionist;

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public ReceptionistEntity getReceptionist() {
        return receptionist;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    public void setReceptionist(ReceptionistEntity receptionist) {
        this.receptionist = receptionist;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }


}

