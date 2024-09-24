package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Role;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.RoleRepository;
import com.workintech.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void register_WhenUsernameAlreadyExists_ShouldThrowException() {
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.register("existingUser", "password"));
        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository).findByUsername("existingUser");
    }

    @Test
    void register_WhenRoleNotFound_ShouldThrowApiException() {
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> authService.register("newUser", "password"));
        assertEquals("Role does not exist", exception.getMessage());

        verify(userRepository).findByUsername("newUser");
        verify(roleRepository).findByAuthority("USER");
    }

    @Test
    void register_WhenValid_ShouldSaveUser() {
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Simulate the role not found initially, but the role is created afterward
        Role userRole = new Role();
        userRole.setAuthority("USER");

        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(userRole));

        User savedUser = new User();
        savedUser.setUsername("newUser");
        savedUser.setPassword("encodedPassword");
        savedUser.setAuthorities(Collections.singleton(userRole));

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.register("newUser", "password");

        assertEquals("newUser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getAuthorities().contains(userRole));

        verify(userRepository).findByUsername("newUser");
        verify(passwordEncoder).encode("password");
        verify(roleRepository).findByAuthority("USER");
        verify(userRepository).save(any(User.class));
    }



}

