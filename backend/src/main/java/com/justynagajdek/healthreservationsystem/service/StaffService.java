package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import com.justynagajdek.healthreservationsystem.entity.NurseEntity;
import com.justynagajdek.healthreservationsystem.entity.ReceptionistEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.DoctorRepository;
import com.justynagajdek.healthreservationsystem.repository.NurseRepository;
import com.justynagajdek.healthreservationsystem.repository.ReceptionistRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StaffService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PasswordEncoder passwordEncoder;
    private final NurseRepository nurseRepository;


    public StaffService(UserRepository userRepository,
                        DoctorRepository doctorRepository,
                        ReceptionistRepository receptionistRepository,
                        PasswordEncoder passwordEncoder,
                        NurseRepository nurseRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.receptionistRepository = receptionistRepository;
        this.passwordEncoder = passwordEncoder;
        this.nurseRepository = nurseRepository;
    }

    public void registerDoctor(SignUpDto dto) {
        checkEmailAvailability(dto.getEmail());

        UserEntity user = createBaseUser(dto, Role.DOCTOR);
        user.setStatus(AccountStatus.ACTIVE);

        UserEntity savedUser = userRepository.save(user);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setUser(savedUser);
        doctor.setSpecialization("UNDEFINED");
        doctorRepository.save(doctor);
    }

    public void registerReceptionist(SignUpDto dto) {
        checkEmailAvailability(dto.getEmail());

        UserEntity user = createBaseUser(dto, Role.RECEPTIONIST);
        user.setStatus(AccountStatus.ACTIVE);

        UserEntity savedUser = userRepository.save(user);

        ReceptionistEntity receptionist = new ReceptionistEntity();
        receptionist.setUser(savedUser);
        receptionistRepository.save(receptionist);
    }

    private void checkEmailAvailability(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

    private UserEntity createBaseUser(SignUpDto dto, Role role) {
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhone());
        user.setRole(role);
        return user;
    }

    public void registerNurse(SignUpDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhone());
        user.setRole(Role.NURSE);
        user.setStatus(AccountStatus.ACTIVE);

        UserEntity savedUser = userRepository.save(user);

        NurseEntity nurse = new NurseEntity();
        nurse.setUser(savedUser);
        nurseRepository.save(nurse);
    }

}
