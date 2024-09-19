package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.PaymentDetails;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.entity.PaymentType;
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
class PaymentDetailsRepositoryTest {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private User testUser;
    private Order testOrder;
    private PaymentDetails testPaymentDetails;

    @BeforeEach
    void setUp() {
        // Set up User
        testUser = new User();
        testUser.setEmail("john_doe@example.com");
        testUser.setPhoneNumber("1234567890");
        testUser.setUsername("john_doe");
        testUser.setPassword("password123");
        userRepository.save(testUser);

        // Set up Order
        testOrder = new Order(); // Assuming Order class is properly implemented
        testOrder.setUser(testUser);
        orderRepository.save(testOrder);

        // Set up PaymentDetails
        testPaymentDetails = new PaymentDetails();
        testPaymentDetails.setUser(testUser);
        testPaymentDetails.setPaymentType(PaymentType.CREDIT_CARD); // Assuming PaymentType enum exists
        testPaymentDetails.setOrder(testOrder);
        paymentDetailsRepository.save(testPaymentDetails);
    }

    @DisplayName("Can save and retrieve PaymentDetails by order ID")
    @Test
    void saveAndFindByOrderId() {
        PaymentDetails retrievedPaymentDetails = paymentDetailsRepository.findByOrder(testOrder);
        assertNotNull(retrievedPaymentDetails, "PaymentDetails should be found by order ID.");
        assertEquals(testOrder.getId(), retrievedPaymentDetails.getOrder().getId(), "Order ID should match.");
    }

    @DisplayName("Can update PaymentDetails")
    @Test
    void updatePaymentDetails() {
        PaymentDetails retrievedPaymentDetails = paymentDetailsRepository.findByOrder(testOrder);
        assertNotNull(retrievedPaymentDetails, "PaymentDetails should be found by order ID.");

        retrievedPaymentDetails.setPaymentType(PaymentType.PAYPAL); // Assuming PaymentType enum exists
        paymentDetailsRepository.save(retrievedPaymentDetails);

        PaymentDetails updatedPaymentDetails = paymentDetailsRepository.findByOrder(testOrder);
        assertNotNull(updatedPaymentDetails, "Updated PaymentDetails should be found.");
        assertEquals(PaymentType.PAYPAL, updatedPaymentDetails.getPaymentType(), "PaymentType should match updated value.");
    }

    @DisplayName("Can delete PaymentDetails")
    @Test
    void deletePaymentDetails() {
        PaymentDetails retrievedPaymentDetails = paymentDetailsRepository.findByOrderId(testOrder.getId());
        assertNotNull(retrievedPaymentDetails, "PaymentDetails should be found by order ID.");

        paymentDetailsRepository.delete(retrievedPaymentDetails);

        PaymentDetails deletedPaymentDetails = paymentDetailsRepository.findByOrderId(testOrder.getId());
        assertNull(deletedPaymentDetails, "PaymentDetails should be deleted and not found.");
    }

    @DisplayName("Cannot find PaymentDetails by nonexistent order ID")
    @Test
    void findByOrderIdNotFound() {
        PaymentDetails paymentDetails = paymentDetailsRepository.findByOrderId(-1L); // Assuming -1L is a nonexistent ID
        assertNull(paymentDetails, "PaymentDetails should not be found by nonexistent order ID.");
    }
}
