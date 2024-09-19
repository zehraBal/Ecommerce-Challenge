package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.OrderItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private Order testOrder;
    private Product testProduct;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        // Create and save a testProduct
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setStockQuantity(10);
        testProduct.setPrice(100.0);
        productRepository.save(testProduct);

        // Create and save a testOrder
        testOrder = new Order();
        testOrder.setTotal(1000.0);
        orderRepository.save(testOrder);

        // Create and save a testOrderItem
        testOrderItem = new OrderItem();
        testOrderItem.setOrder(testOrder);
        testOrderItem.setProduct(testProduct);
        testOrderItem.setQuantity(5);
        testOrderItem.setPrice(100.0);
        testOrderItem.setTotal(500.0);
        orderItemRepository.save(testOrderItem);
    }

    @DisplayName("Can save and retrieve OrderItem by ID")
    @Test
    void saveAndFindById() {
        OrderItem retrievedOrderItem = orderItemRepository.findById(testOrderItem.getId()).orElse(null);
        assertNotNull(retrievedOrderItem, "OrderItem should be found by ID.");
        assertEquals(testOrderItem.getId(), retrievedOrderItem.getId(), "OrderItem ID should match.");
    }

    @DisplayName("Can find OrderItems by Order ID")
    @Test
    void findByOrderId() {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(testOrder.getId());
        assertFalse(orderItems.isEmpty(), "OrderItems should be found by Order ID.");
        assertTrue(orderItems.stream().anyMatch(oi -> oi.getId().equals(testOrderItem.getId())), "OrderItems should include the saved OrderItem.");
    }

    @DisplayName("Can find OrderItems by Product ID")
    @Test
    void findByProductId() {
        List<OrderItem> orderItems = orderItemRepository.findByProductId(testProduct.getId());
        assertFalse(orderItems.isEmpty(), "OrderItems should be found by Product ID.");
        assertTrue(orderItems.stream().anyMatch(oi -> oi.getId().equals(testOrderItem.getId())), "OrderItems should include the saved OrderItem.");
    }

    @DisplayName("Can update OrderItem")
    @Test
    void updateOrderItem() {
        testOrderItem.setQuantity(10);
        testOrderItem.setTotal(1000.0);
        orderItemRepository.save(testOrderItem);

        OrderItem updatedOrderItem = orderItemRepository.findById(testOrderItem.getId()).orElse(null);
        assertNotNull(updatedOrderItem, "Updated OrderItem should be found by ID.");
        assertEquals(10, updatedOrderItem.getQuantity(), "Quantity should match updated value.");
        assertEquals(1000.0, updatedOrderItem.getTotal(), "Total should match updated value.");
    }

    @DisplayName("Can delete OrderItem")
    @Test
    void deleteOrderItem() {
        orderItemRepository.delete(testOrderItem);

        Optional<OrderItem> deletedOrderItem = orderItemRepository.findById(testOrderItem.getId());
        assertFalse(deletedOrderItem.isPresent(), "OrderItem should be deleted and not found.");
    }
}
