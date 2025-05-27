package com.justynagajdek.healthreservationsystem.dto;

public record PendingUserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        String pesel,
        String address,
        String role
) {}

