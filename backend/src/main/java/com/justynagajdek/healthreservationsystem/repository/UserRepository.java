package com.justynagajdek.healthreservationsystem.repository;

import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByStatus(AccountStatus status);
}
