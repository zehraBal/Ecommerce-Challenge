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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        testRole = new Role();
        testRole.setAuthority("USER");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("testuser@example.com");
        testUser.setPhoneNumber("1234567890");
        testUser.setShoppingCart(null);
        testUser.setOrders(null);
        testUser.setAuthorities(new HashSet<>(Collections.singletonList(testRole)));
    }

    @Test
    void findAll_NoUsersFound() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> userService.findAll());
        assertEquals("No users found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void findAll_UsersFound() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testUser.getUsername(), result.get(0).getUsername());
    }

    @Test
    void findById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> userService.findById(1L));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void findById_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void findByEmail_UserNotFound() {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> userService.findByEmail("testuser@example.com"));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void findByEmail_UserFound() {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(testUser);

        User result = userService.findByEmail("testuser@example.com");

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void findByPhoneNumber_UserNotFound() {
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> userService.findByPhoneNumber("1234567890"));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void findByPhoneNumber_UserFound() {
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(testUser);

        User result = userService.findByPhoneNumber("1234567890");

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void save_UserAlreadyExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        ApiException exception = assertThrows(ApiException.class, () -> userService.save(testUser));
        assertEquals("Username already exists", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void save_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedpassword");
        when(userRepository.save(testUser)).thenReturn(testUser);

        User result = userService.save(testUser);

        assertNotNull(result);
        assertEquals("encodedpassword", result.getPassword());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void update_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> userService.update(1L, testUser));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void update_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("password")).thenReturn("encodedpassword");
        when(userRepository.save(testUser)).thenReturn(testUser);

        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("password");
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Smith");
        updatedUser.setEmail("updateduser@example.com");
        updatedUser.setPhoneNumber("0987654321");
        updatedUser.setShoppingCart(null);
        updatedUser.setOrders(null);
        updatedUser.setAuthorities(new HashSet<>(Collections.singletonList(testRole)));

        User result = userService.update(1L, updatedUser);

        assertNotNull(result);
        assertEquals("updateduser", result.getUsername());
        assertEquals("encodedpassword", result.getPassword());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("updateduser@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhoneNumber());
    }

    @Test
    void delete_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> userService.delete(1L));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void delete_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        User result = userService.delete(1L);

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testuser"));
        assertEquals("User credentials are not valid", exception.getMessage());
    }

    @Test
    void loadUserByUsername_UserFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails result = userService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
    }
}
