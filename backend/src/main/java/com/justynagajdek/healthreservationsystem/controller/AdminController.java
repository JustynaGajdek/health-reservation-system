package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.UserDto;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.mapper.UserMapper;
import com.justynagajdek.healthreservationsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AdminController(UserService userService, UserMapper userMapper) {

        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        log.debug("Accessing admin dashboard");
        return "Admin Dashboard - Users, Doctors, Patients, Receptionists";
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        return userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .toList();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id={}", id);
        userService.deleteUser(id);
    }

    @GetMapping("/users/pending")
    public List<UserDto> getPendingUsers() {
        log.info("Fetching pending users");
        return userService.getUsersByStatus(AccountStatus.PENDING).stream()
                .map(userMapper::toDto)
                .toList();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/{id}/approve")
    public void approveUser(@PathVariable Long id) {
        log.info("Approving user with id={}", id);
        userService.approveUser(id);
    }

}
