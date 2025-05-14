package com.justynagajdek.healthreservationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nurses")
@Getter
@Setter
@NoArgsConstructor
public class NurseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
}
