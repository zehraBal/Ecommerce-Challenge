package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.ShoppingCart;
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
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private ShoppingCart testCart;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("john_doe@example.com");
        testUser.setPhoneNumber("1234567890");
        testUser.setUsername("john_doe");
        testUser.setPassword("password123");
        userRepository.save(testUser);

        testCart = new ShoppingCart();
        testCart.setUser(testUser);
        shoppingCartRepository.save(testCart);
    }

    @DisplayName("Can save and retrieve ShoppingCart by ID")
    @Test
    void saveAndFindById() {
        Optional<ShoppingCart> retrievedCart = shoppingCartRepository.findById(testCart.getId());
        assertTrue(retrievedCart.isPresent(), "ShoppingCart should be found by ID.");
        assertEquals(testCart.getId(), retrievedCart.get().getId(), "ShoppingCart ID should match.");
    }

    @DisplayName("Can delete ShoppingCart")
    @Test
    void deleteShoppingCart() {
        shoppingCartRepository.delete(testCart);

        // Sildikten sonra sepeti bulmaya çalışıyoruz
        Optional<ShoppingCart> deletedCart = shoppingCartRepository.findById(testCart.getId());
        assertFalse(deletedCart.isPresent(), "ShoppingCart should be deleted and not found.");
    }

    @DisplayName("Can update ShoppingCart")
    @Test
    void updateShoppingCart() {

        testCart.setTotalPrice(100.0);
        shoppingCartRepository.save(testCart);

        Optional<ShoppingCart> updatedCart = shoppingCartRepository.findById(testCart.getId());
        assertTrue(updatedCart.isPresent(), "Updated ShoppingCart should be found.");
        assertEquals(100.0, updatedCart.get().getTotalPrice(), "Total price should match updated value.");
    }


}