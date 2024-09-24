package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.dto.ProductRequest;
import com.workintech.ecommerce.dto.ProductResponse;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.service.CategoryService;
import com.workintech.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService, categoryService)).build();
    }

    @Test
    void getAll_ShouldReturnListOfProducts() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setStockQuantity(10);
        product.setPrice(1000.0);

        Category category = new Category();
        category.setName("Electronics");
        product.setCategory(category);

        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(1000.0))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"));

        verify(productService).findAll();
    }

    @Test
    void getById_ShouldReturnProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setStockQuantity(10);
        product.setPrice(1000.0);

        Category category = new Category();
        category.setName("Electronics");
        product.setCategory(category);

        when(productService.findById(1L)).thenReturn(product);

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000.0))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        verify(productService).findById(1L);
    }

    @Test
    void getByCategory_ShouldReturnProductsByCategoryId() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setStockQuantity(10);
        product.setPrice(1000.0);

        Category category = new Category();
        category.setName("Electronics");
        product.setCategory(category);

        when(productService.findByCategory(1L)).thenReturn(List.of(product));

        mockMvc.perform(get("/product/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(1000.0))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"));

        verify(productService).findByCategory(1L);
    }

    @Test
    void save_ShouldCreateNewProduct() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setStockQuantity(10);
        product.setPrice(1000.0);
        product.setCategory(category);

        ProductRequest productRequest = new ProductRequest("Laptop", 10, 1000.0, 1L);

        when(categoryService.findById(1L)).thenReturn(category);
        when(productService.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Laptop",
                                    "stockQuantity": 10,
                                    "price": 1000.0,
                                    "category_id": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000.0))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        verify(categoryService).findById(1L);
        verify(productService).save(any(Product.class));
    }

    @Test
    void update_ShouldUpdateExistingProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setStockQuantity(10);
        product.setPrice(1000.0);

        Category category = new Category();
        category.setName("Electronics");
        product.setCategory(category);

        when(productService.update(anyLong(), any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Laptop",
                                    "stockQuantity": 5,
                                    "price": 900.0
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000.0));

        verify(productService).update(anyLong(), any(Product.class));
    }

    @Test
    void delete_ShouldDeleteProductById() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setCategory(category);

        when(productService.delete(1L)).thenReturn(product);

        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        verify(productService).delete(1L);
    }

    @Test
    void decreaseQuantity_ShouldDecreaseProductQuantity() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setStockQuantity(10);
        product.setCategory(category);

        when(productService.decreaseQuantity(1L, 5)).thenReturn(product);

        mockMvc.perform(put("/product/1/decreaseQuantity")
                        .param("decNum", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity").value(10))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));  // Kategori adı kontrol ediliyor

        verify(productService).decreaseQuantity(1L, 5);
    }

    @Test
    void increaseQuantity_ShouldIncreaseProductQuantity() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setStockQuantity(10);
        product.setCategory(category);


        when(productService.increaseQuantity(1L, 5)).thenReturn(product);

        mockMvc.perform(put("/product/1/increaseQuantity")
                        .param("incNum", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity").value(10))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        verify(productService).increaseQuantity(1L, 5);
    }


}
