package com.justynagajdek.healthreservationsystem.controller;

import com.justynagajdek.healthreservationsystem.dto.PatientDto;
import com.justynagajdek.healthreservationsystem.dto.UserDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.mapper.PatientMapper;
import com.justynagajdek.healthreservationsystem.mapper.UserMapper;
import com.justynagajdek.healthreservationsystem.service.PatientService;
import com.justynagajdek.healthreservationsystem.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/patient",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;
    private final PatientMapper patientMapper;
    private final UserMapper userMapper;

    public PatientController(PatientService patientService,
                             UserService userService,
                             PatientMapper patientMapper,
                             UserMapper userMapper) {
        this.patientService = patientService;
        this.userService = userService;
        this.patientMapper = patientMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(Authentication auth) {
        UserEntity entity = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(userMapper.toDto(entity));
    }

    @PutMapping(path = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateProfile(Authentication auth,
                                                 @RequestBody UserDto dto) {
        UserEntity updated = userService.updateProfile(auth.getName(), dto);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR')")
    @GetMapping("/by-pesel/{pesel}")
    public ResponseEntity<PatientDto> getByPesel(@PathVariable String pesel) {
        PatientEntity entity = patientService.getByPesel(pesel);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        PatientDto dto = patientMapper.toDto(entity);
        return ResponseEntity.ok(dto);
    }
}
