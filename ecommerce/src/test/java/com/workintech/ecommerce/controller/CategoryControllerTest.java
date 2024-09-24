package com.workintech.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.ecommerce.dto.CategoryRequest;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void getAll_ShouldReturnListOfCategories() throws Exception {
        when(categoryService.findAll()).thenReturn(List.of(category));

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));

        verify(categoryService).findAll();
    }

    @Test
    void getById_ShouldReturnCategoryById() throws Exception {
        when(categoryService.findById(1L)).thenReturn(category);

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryService).findById(1L);
    }

    @Test
    void save_ShouldCreateNewCategory() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest("Electronics");
        when(categoryService.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryService).save(any(Category.class));
    }

    @Test
    void update_ShouldUpdateCategory() throws Exception {
        when(categoryService.update(eq(1L), any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryService).update(eq(1L), any(Category.class));
    }

    @Test
    void delete_ShouldDeleteCategory() throws Exception {
        when(categoryService.delete(1L)).thenReturn(category);

        mockMvc.perform(delete("/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(categoryService).delete(1L);
    }

    // Helper method to convert objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
