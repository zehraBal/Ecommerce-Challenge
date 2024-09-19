package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("john_doe@example.com");
        testUser.setPhoneNumber("1234567890");
        testUser.setUsername("jane_doe");
        testUser.setPassword("password123");
        userRepository.save(testUser);
    }

    @DisplayName("Can find user by email")
    @Test
    void findByEmail() {
        User user = userRepository.findByEmail("john_doe@example.com");
        assertNotNull(user, "User should be found by email.");
        assertEquals("john_doe@example.com", user.getEmail(), "Email should match.");
    }

    @DisplayName("Can find user by phone number")
    @Test
    void findByPhoneNumber() {
        User user = userRepository.findByPhoneNumber("1234567890");
        assertNotNull(user, "User should be found by phone number.");
        assertEquals("1234567890", user.getPhoneNumber(), "Phone number should match.");
    }

    @DisplayName("Can find user by username")
    @Test
    void findByUsername() {
        Optional<User> user = userRepository.findByUsername("jane_doe");
        assertTrue(user.isPresent(), "User should be found by username.");
        assertEquals("jane_doe", user.get().getUsername(), "Username should match.");
    }

    @DisplayName("Cannot find user by nonexistent email")
    @Test
    void findByEmailNotFound() {
        User user = userRepository.findByEmail("nonexistent@example.com");
        assertNull(user, "User should not be found by nonexistent email.");
    }

    @DisplayName("Cannot find user by nonexistent phone number")
    @Test
    void findByPhoneNumberNotFound() {
        User user = userRepository.findByPhoneNumber("0000000000");
        assertNull(user, "User should not be found by nonexistent phone number.");
    }

    @DisplayName("Cannot find user by nonexistent username")
    @Test
    void findByUsernameNotFound() {
        Optional<User> user = userRepository.findByUsername("nonexistentuser");
        assertFalse(user.isPresent(), "User should not be found by nonexistent username.");
    }
}