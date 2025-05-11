package com.justynagajdek.healthreservationsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Health Reservation System API",
                version = "1.0",
                description = "API for managing users, appointments, and prescriptions"
        )
)
@Configuration
public class OpenApiConfig {}
