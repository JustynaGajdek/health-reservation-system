package com.justynagajdek.healthreservationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

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

    @Type(JsonBinaryType.class)
    @Column(name = "working_hours", columnDefinition = "jsonb")
    private Map<String, String> workingHours;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToMany(mappedBy = "doctor")
    private List<AppointmentEntity> appointments;

    public void setFirstName(String anna) {
    }

    public void setLastName(String nowak) {
    }
}
