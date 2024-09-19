package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
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
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository; // Assuming you have a CategoryRepository

    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setName("Electronics");
        categoryRepository.save(testCategory);

        testProduct = new Product();
        testProduct.setName("Smartphone");
        testProduct.setStockQuantity(10); // Valid stock quantity
        testProduct.setPrice(299.99); // Valid price
        testProduct.setCategory(testCategory);
        productRepository.save(testProduct);
    }

    @DisplayName("Can find products by category")
    @Test
    void findByCategory() {
        List<Product> products = productRepository.findByCategory(testCategory.getId());
        assertFalse(products.isEmpty(), "Products should be found by category.");
        assertEquals(testProduct.getName(), products.get(0).getName(), "Product name should match.");
    }

    @DisplayName("Can find product by name")
    @Test
    void findByProductName() {
        Product product = productRepository.findByProductName("Smartphone");
        assertNotNull(product, "Product should be found by name.");
        assertEquals("Smartphone", product.getName(), "Product name should match.");
    }

    @DisplayName("Cannot find product by nonexistent name")
    @Test
    void findByProductNameNotFound() {
        Product product = productRepository.findByProductName("Nonexistent Product");
        assertNull(product, "Product should not be found by nonexistent name.");
    }

    @DisplayName("Cannot save product with invalid stock quantity")
    @Test
    void saveProductWithInvalidStockQuantity() {
        Product invalidProduct = new Product();
        invalidProduct.setName("Invalid Product");
        invalidProduct.setStockQuantity(0); // Invalid stock quantity
        invalidProduct.setPrice(100.00);
        invalidProduct.setCategory(testCategory);

        assertThrows(Exception.class, () -> {
            productRepository.save(invalidProduct);
        }, "Saving product with invalid stock quantity should throw an exception.");
    }

    @DisplayName("Cannot save product with invalid price")
    @Test
    void saveProductWithInvalidPrice() {
        Product invalidProduct = new Product();
        invalidProduct.setName("Invalid Product");
        invalidProduct.setStockQuantity(10);
        invalidProduct.setPrice(-1.00); // Invalid price
        invalidProduct.setCategory(testCategory);

        assertThrows(Exception.class, () -> {
            productRepository.save(invalidProduct);
        }, "Saving product with invalid price should throw an exception.");
    }
}
