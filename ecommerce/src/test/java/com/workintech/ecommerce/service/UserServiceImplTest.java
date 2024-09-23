package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Role;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void findAll_WhenUsersFound_ShouldReturnList() {
        User mockUser = new User();
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals(mockUser, result.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void findAll_WhenNoUsersFound_ShouldThrowException() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> userService.findAll());
        assertEquals("No users found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(userRepository).findAll();
    }

    @Test
    void findById_WhenUserFound_ShouldReturnUser() {
        User mockUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User result = userService.findById(1L);

        assertEquals(mockUser, result);
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> userService.findById(1L));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(userRepository).findById(1L);
    }

    @Test
    void findByEmail_WhenUserFound_ShouldReturnUser() {
        User mockUser = new User();
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        User result = userService.findByEmail("test@example.com");

        assertEquals(mockUser, result);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void findByEmail_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> userService.findByEmail("test@example.com"));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void findByPhoneNumber_WhenUserFound_ShouldReturnUser() {
        User mockUser = new User();
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(mockUser);

        User result = userService.findByPhoneNumber("1234567890");

        assertEquals(mockUser, result);
        verify(userRepository).findByPhoneNumber("1234567890");
    }

    @Test
    void findByPhoneNumber_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> userService.findByPhoneNumber("1234567890"));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(userRepository).findByPhoneNumber("1234567890");
    }

    @Test
    void save_WhenUserIsNew_ShouldSaveUserWithEncodedPasswordAndDefaultRole() {
        User mockUser = new User();
        mockUser.setUsername("newUser");
        mockUser.setPassword("password");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.save(mockUser);

        assertEquals(mockUser, result);
        assertEquals("encodedPassword", mockUser.getPassword());
        assertTrue(mockUser.getAuthorities().stream().anyMatch(role -> "USER".equals(role.getAuthority())));
        verify(userRepository).save(mockUser);
        verify(passwordEncoder).encode("password");
    }

    @Test
    void save_WhenUsernameAlreadyExists_ShouldThrowException() {
        User mockUser = new User();
        mockUser.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(mockUser));

        ApiException exception = assertThrows(ApiException.class, () -> userService.save(mockUser));
        assertEquals("Username already exists", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        verify(userRepository).findByUsername("existingUser");
    }

    @Test
    void update_WhenUserExists_ShouldUpdateUserFields() {
        User mockUser = new User();
        mockUser.setUsername("existingUser");
        mockUser.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(1L, updatedUser);

        assertEquals("updatedUser", mockUser.getUsername());
        assertEquals("encodedNewPassword", mockUser.getPassword());
        verify(userRepository).findById(1L);
        verify(userRepository).save(mockUser);
    }

    @Test
    void delete_WhenUserExists_ShouldDeleteUser() {
        User mockUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User result = userService.delete(1L);

        assertEquals(mockUser, result);
        verify(userRepository).findById(1L);
        verify(userRepository).delete(mockUser);
    }

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        User mockUser = new User();
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(mockUser));

        UserDetails result = userService.loadUserByUsername("existingUser");

        assertEquals(mockUser, result);
        verify(userRepository).findByUsername("existingUser");
    }

    @Test
    void loadUserByUsername_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("unknownUser"));
        assertEquals("User credentials are not valid", exception.getMessage());
        verify(userRepository).findByUsername("unknownUser");
    }
}
