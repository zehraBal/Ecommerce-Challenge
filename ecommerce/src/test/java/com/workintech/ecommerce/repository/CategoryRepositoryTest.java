package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
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
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Create and save a testCategory
        testCategory = new Category();
        testCategory.setName("Electronics");
        categoryRepository.save(testCategory);

        // Create and save a testProduct
        testProduct = new Product();
        testProduct.setName("Laptop");
        testProduct.setStockQuantity(10);
        testProduct.setPrice(999.99);
        testProduct.setCategory(testCategory);
        productRepository.save(testProduct);
    }

    @DisplayName("Can save and retrieve Category by ID")
    @Test
    void saveAndFindById() {
        Category retrievedCategory = categoryRepository.findById(testCategory.getId()).orElse(null);
        assertNotNull(retrievedCategory, "Category should be found by ID.");
        assertEquals(testCategory.getId(), retrievedCategory.getId(), "Category ID should match.");
    }

    @DisplayName("Can find Category by name")
    @Test
    void findByName() {
        List<Category> categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty(), "Categories should be found.");
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals(testCategory.getName())), "Category with specified name should be present.");
    }

    @DisplayName("Can update Category")
    @Test
    void updateCategory() {
        testCategory.setName("Home Appliances");
        categoryRepository.save(testCategory);

        Category updatedCategory = categoryRepository.findById(testCategory.getId()).orElse(null);
        assertNotNull(updatedCategory, "Updated Category should be found by ID.");
        assertEquals("Home Appliances", updatedCategory.getName(), "Category name should match updated value.");
    }

    @DisplayName("Can delete Category")
    @Test
    void deleteCategory() {
        categoryRepository.delete(testCategory);

        Optional<Category> deletedCategory = categoryRepository.findById(testCategory.getId());
        assertFalse(deletedCategory.isPresent(), "Category should be deleted and not found.");
    }
}
