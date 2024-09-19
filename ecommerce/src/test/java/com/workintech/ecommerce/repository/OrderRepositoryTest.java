package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.*;
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
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    private User testUser;
    private ShoppingCart testCart;
    private PaymentDetails testPaymentDetails;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        // Create and save testUser
        testUser = new User();
        testUser.setEmail("john_doe@example.com");
        testUser.setPhoneNumber("1234567890");
        testUser.setUsername("john_doe");
        testUser.setPassword("password123");
        userRepository.save(testUser);

        // Create and save testShoppingCart
        testCart = new ShoppingCart();
        testCart.setUser(testUser);
        shoppingCartRepository.save(testCart);

        // Create and save testPaymentDetails
        testPaymentDetails = new PaymentDetails();
        testPaymentDetails.setUser(testUser);
        testPaymentDetails.setPaymentType(PaymentType.CREDIT_CARD); // Assume PaymentType is an enum
        paymentDetailsRepository.save(testPaymentDetails);

        // Create and save testOrder
        testOrder = new Order();
        testOrder.setUser(testUser);
        testOrder.setTotal(150.0);
        testOrder.setPaymentDetails(testPaymentDetails);
        testOrder.setShoppingCart(testCart);
        orderRepository.save(testOrder);
    }

    @DisplayName("Can save and retrieve Order by ID")
    @Test
    void saveAndFindById() {
        Order retrievedOrder = orderRepository.findById(testOrder.getId()).orElse(null);
        assertNotNull(retrievedOrder, "Order should be found by ID.");
        assertEquals(testOrder.getId(), retrievedOrder.getId(), "Order ID should match.");
    }

    @DisplayName("Can find Order by User")
    @Test
    void findByUser() {
        Order order = orderRepository.findByUser(testUser);
        assertNotNull(order, "Order should be found by user.");
        assertEquals(testUser.getId(), order.getUser().getId(), "User ID should match.");
    }

    @DisplayName("Can update Order")
    @Test
    void updateOrder() {
        testOrder.setTotal(200.0);
        orderRepository.save(testOrder);

        Order updatedOrder = orderRepository.findById(testOrder.getId()).orElse(null);
        assertNotNull(updatedOrder, "Updated Order should be found by ID.");
        assertEquals(200.0, updatedOrder.getTotal(), "Total price should match updated value.");
    }

    @DisplayName("Can delete Order")
    @Test
    void deleteOrder() {
        orderRepository.delete(testOrder);

        Optional<Order> deletedOrder = orderRepository.findById(testOrder.getId());
        assertFalse(deletedOrder.isPresent(), "Order should be deleted and not found.");
    }
}
