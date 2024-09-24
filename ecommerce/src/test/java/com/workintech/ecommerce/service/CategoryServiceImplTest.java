package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void findAll_ShouldReturnCategories() {
        Category category = new Category();
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.findAll();

        assertEquals(1, result.size());
        assertEquals(category, result.get(0));
        verify(categoryRepository).findAll();
    }

    @Test
    void findAll_WhenNoCategoriesFound_ShouldReturnEmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.findAll();

        assertTrue(result.isEmpty());
        verify(categoryRepository).findAll();
    }

    @Test
    void findById_ShouldReturnCategory() {
        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertEquals(category, result);
        verify(categoryRepository).findById(1L);
    }

    @Test
    void findById_WhenCategoryNotFound_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> categoryService.findById(1L));
        assertEquals("Category not found.", exception.getMessage());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void save_ShouldSaveCategory() {
        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.findByName("Electronics")).thenReturn(null);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.save(category);

        assertEquals(category, result);
        verify(categoryRepository).findByName("Electronics");
        verify(categoryRepository).save(category);
    }

    @Test
    void save_WhenCategoryAlreadyExists_ShouldThrowException() {
        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.findByName("Electronics")).thenReturn(category);

        ApiException exception = assertThrows(ApiException.class, () -> categoryService.save(category));
        assertEquals("Category with the name 'Electronics' already exists.", exception.getMessage());
        verify(categoryRepository).findByName("Electronics");
    }

    @Test
    void save_WhenCategoryIsNull_ShouldThrowException() {
        ApiException exception = assertThrows(ApiException.class, () -> categoryService.save(null));
        assertEquals("Category cannot be null", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateCategory() {
        Category existingCategory = new Category();
        existingCategory.setId(1L);

        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        Category result = categoryService.update(1L, updatedCategory);

        assertEquals("Updated Category", existingCategory.getName());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(existingCategory);
    }

    @Test
    void update_WhenCategoryNotFound_ShouldThrowException() {
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> categoryService.update(1L, updatedCategory));
        assertEquals("Category not found.", exception.getMessage());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void delete_ShouldDeleteCategory() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.delete(1L);

        assertEquals(category, result);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(category);
    }

    @Test
    void delete_WhenCategoryNotFound_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> categoryService.delete(1L));
        assertEquals("Category not found.", exception.getMessage());
        verify(categoryRepository).findById(1L);
    }
}
