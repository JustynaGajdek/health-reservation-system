package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import com.justynagajdek.healthreservationsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterNewUserSuccessfully() {
        SignUpDto dto = new SignUpDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setFirstName("Anna");
        dto.setLastName("Nowak");
        dto.setPhone("123456789");
        dto.setRole("PATIENT");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserEntity savedUser = userService.registerNewUser(dto);

        assertEquals(dto.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPasswordHash());
        assertEquals(Role.PATIENT, savedUser.getRole());
        assertEquals(AccountStatus.PENDING, savedUser.getStatus());
    }

    @Test
    void shouldThrowExceptionIfEmailAlreadyExists() {
        SignUpDto dto = new SignUpDto();
        dto.setEmail("existing@example.com");

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(new UserEntity()));

        assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(dto));
    }

    @Test
    void shouldReturnAllUsers() {
        List<UserEntity> mockUsers = List.of(new UserEntity(), new UserEntity());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserEntity> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void shouldDeleteUserById() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        UserEntity user = new UserEntity();
        user.setEmail("login@example.com");
        user.setPasswordHash("secret");
        user.setRole(Role.PATIENT);
        user.setStatus(AccountStatus.ACTIVE);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());

        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPasswordHash(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT")));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }
}
