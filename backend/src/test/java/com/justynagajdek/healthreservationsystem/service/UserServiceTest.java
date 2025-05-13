package com.justynagajdek.healthreservationsystem.service;

import com.justynagajdek.healthreservationsystem.dto.SignUpDto;
import com.justynagajdek.healthreservationsystem.dto.UserDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.exception.ResourceNotFoundException;
import com.justynagajdek.healthreservationsystem.exception.UserNotFoundException;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
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
    void shouldDeleteUserWhenExists() {
        // given
        Long userId = 1L;
        UserEntity existing = new UserEntity();
        existing.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));

        // when
        userService.deleteUser(userId);

        // then
        verify(userRepository, times(1)).delete(existing);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // given
        Long userId = 42L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
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

    @Test
    void shouldReturnUsersByStatus() {
        UserEntity u1 = new UserEntity();
        u1.setStatus(AccountStatus.PENDING);

        UserEntity u2 = new UserEntity();
        u2.setStatus(AccountStatus.ACTIVE);

        when(userRepository.findByStatus(AccountStatus.PENDING)).thenReturn(List.of(u1));

        List<UserEntity> result = userService.getUsersByStatus(AccountStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(AccountStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void shouldThrowWhenApprovingNonexistentUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.approveUser(99L));
    }

    @Test
    void shouldRegisterUserWithDefaultRoleWhenRoleIsNull() {
        SignUpDto dto = new SignUpDto();
        dto.setEmail("no-role@example.com");
        dto.setPassword("abc");
        dto.setFirstName("No");
        dto.setLastName("Role");
        dto.setPhone("123456789");
        dto.setRole(null);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserEntity saved = userService.registerNewUser(dto);

        assertEquals(Role.PATIENT, saved.getRole(), "Default role should be PATIENT");
    }

    @Test
    void shouldApproveUserWhenExists() {
        Long id = 5L;
        UserEntity u = new UserEntity();
        u.setId(id);
        u.setStatus(AccountStatus.PENDING);
        when(userRepository.findById(id)).thenReturn(Optional.of(u));

        userService.approveUser(id);

        assertEquals(AccountStatus.ACTIVE, u.getStatus());
        verify(userRepository).save(u);
    }



    @Test
    void shouldUpdateUserProfileSuccessfully() {
        // given
        String email = "anna@example.com";
        UserDto dto = new UserDto();
        dto.setFirstName("Anna");
        dto.setLastName("Nowak");
        dto.setPhone("123456789");

        UserEntity existing = new UserEntity();
        existing.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        // when
        UserEntity result = userService.updateProfile(email, dto);

        // then
        assertEquals("Anna", result.getFirstName());
        assertEquals("Nowak", result.getLastName());
        assertEquals("123456789", result.getPhoneNumber());
        verify(userRepository).save(existing);
    }

    @Test
    void shouldThrowWhenUpdatingProfileOfNonexistentUser() {
        String email = "missing@example.com";
        UserDto dto = new UserDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateProfile(email, dto));
    }




}
