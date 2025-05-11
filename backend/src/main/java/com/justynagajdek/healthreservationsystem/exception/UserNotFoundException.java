package com.justynagajdek.healthreservationsystem.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Nie znaleziono użytkownika o id=" + id);
    }
}
