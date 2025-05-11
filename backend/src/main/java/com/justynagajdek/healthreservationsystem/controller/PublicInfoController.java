package com.justynagajdek.healthreservationsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class PublicInfoController {

    @GetMapping
    public String getWelcomeMessage() {
        return "Welcome to the Health Reservation System";
    }
}
