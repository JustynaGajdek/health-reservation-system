package com.justynagajdek.healthreservationsystem.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World from Spring Boot!";
    }
}
