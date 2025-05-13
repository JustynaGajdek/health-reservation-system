package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.LoginDto;
import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import com.justynagajdek.healthreservationsystem.payload.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.service.UserService;
import org.springframework.http.HttpStatus;


@RequestMapping("/auth")
@RestController
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService UserService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.UserService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // Jeśli uwierzytelnienie się powiedzie, zostanie wygenerowany token JWT
            String jwt = jwtTokenUtil.generateJwtToken(loginRequest.getEmail());
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Bad credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signupDto) {
        try {
            UserEntity newUser = UserService.registerNewUser(signupDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Account created. Waiting for admin approval.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}