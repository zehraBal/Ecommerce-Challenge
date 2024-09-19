package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    private CartItem testCartItem;
    private Product testProduct;
    private ShoppingCart testShoppingCart;

    @BeforeEach
    void setUp() {
        // Create and save a testProduct
        testProduct = new Product();
        testProduct.setName("Smartphone");
        testProduct.setStockQuantity(15);
        testProduct.setPrice(599.99);
        productRepository.save(testProduct);

        // Create and save a testShoppingCart
        testShoppingCart = new ShoppingCart();
        shoppingCartRepository.save(testShoppingCart);

        // Create and save a testCartItem
        testCartItem = new CartItem();
        testCartItem.setShoppingCart(testShoppingCart);
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2);
        testCartItem.setPrice(599.99);
        cartItemRepository.save(testCartItem);
    }

    @DisplayName("Can save and retrieve CartItem by ID")
    @Test
    void saveAndFindById() {
        CartItem retrievedCartItem = cartItemRepository.findById(testCartItem.getId()).orElse(null);
        assertNotNull(retrievedCartItem, "CartItem should be found by ID.");
        assertEquals(testCartItem.getId(), retrievedCartItem.getId(), "CartItem ID should match.");
    }

    @DisplayName("Can find CartItem by Product ID")
    @Test
    void findByProductId() {
        CartItem retrievedCartItem = cartItemRepository.findByProductId(testProduct.getId());
        assertNotNull(retrievedCartItem, "CartItem should be found by Product ID.");
        assertEquals(testProduct.getId(), retrievedCartItem.getProduct().getId(), "Product ID should match.");
    }

    @DisplayName("Can find CartItems by ShoppingCart ID")
    @Test
    void findByShoppingCart() {
        List<CartItem> cartItems = cartItemRepository.findByShoppingCart(testShoppingCart.getId());
        assertFalse(cartItems.isEmpty(), "CartItems should be found by ShoppingCart ID.");
        assertTrue(cartItems.stream().anyMatch(c -> c.getShoppingCart().getId().equals(testShoppingCart.getId())), "CartItems for the specified ShoppingCart should be present.");
    }

    @DisplayName("Can find CartItem by ShoppingCart and Product")
    @Test
    void findByShoppingCartAndProduct() {
        CartItem retrievedCartItem = cartItemRepository.findByShoppingCartAndProduct(testShoppingCart, testProduct);
        assertNotNull(retrievedCartItem, "CartItem should be found by ShoppingCart and Product.");
        assertEquals(testCartItem.getId(), retrievedCartItem.getId(), "CartItem ID should match.");
    }

    @DisplayName("Can update CartItem")
    @Test
    void updateCartItem() {
        testCartItem.setQuantity(5);
        cartItemRepository.save(testCartItem);

        CartItem updatedCartItem = cartItemRepository.findById(testCartItem.getId()).orElse(null);
        assertNotNull(updatedCartItem, "Updated CartItem should be found by ID.");
        assertEquals(5, updatedCartItem.getQuantity(), "CartItem quantity should match updated value.");
    }

    @DisplayName("Can delete CartItem")
    @Test
    void deleteCartItem() {
        cartItemRepository.delete(testCartItem);

        CartItem deletedCartItem = cartItemRepository.findById(testCartItem.getId()).orElse(null);
        assertNull(deletedCartItem, "CartItem should be deleted and not found.");
    }
}
