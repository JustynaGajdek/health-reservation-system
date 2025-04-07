package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.justynagajdek.healthreservationsystem.dto.SignUpDto;



import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new User(user.getEmail(), user.getPasswordHash(), authorities);

    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntity registerNewUser(SignUpDto signupDto) {
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "Email '" + signupDto.getEmail() + "' is already in use."
            );
        }

        UserEntity user = new UserEntity();
        user.setEmail(signupDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(signupDto.getPassword()));
        user.setFirstName(signupDto.getFirstName());
        user.setLastName(signupDto.getLastName());
        user.setPhoneNumber(signupDto.getPhone());

        if (signupDto.getRole() != null) {
            user.setRole(Role.valueOf(signupDto.getRole().toUpperCase()));
        } else {
            user.setRole(Role.PATIENT);
        }

        user.setStatus(AccountStatus.PENDING);

        return userRepository.save(user);
    }

    public List<UserEntity> getUsersByStatus(AccountStatus status) {
        return userRepository.findByStatus(status);
    }

    public void approveUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

}
