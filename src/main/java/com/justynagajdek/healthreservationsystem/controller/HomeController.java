package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.LoginDto;
import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import com.justynagajdek.healthreservationsystem.payload.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.service.UserService;
import org.springframework.http.HttpStatus;


@RestController
public class HomeController {


    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService UserService;

    public HomeController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.UserService = userService;
    }


    @GetMapping("/")
    public String home() {
        return "Witamy na stronie Przychodni Zdrowia";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Jeśli uwierzytelnienie się powiedzie, zostanie wygenerowany token JWT
        String jwt = jwtTokenUtil.generateJwtToken(loginRequest.getEmail());
        return ResponseEntity.ok(new JwtResponse(jwt));
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