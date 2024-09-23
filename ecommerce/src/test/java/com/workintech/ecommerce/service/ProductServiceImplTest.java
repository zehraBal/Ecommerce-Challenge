package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp(){
        productService=new ProductServiceImpl(productRepository);
    }

   @Test
    void findById() {
       Product product =new Product();
       product.setId(1L);
       when(productRepository.findById(1L)).thenReturn(Optional.of(product));
       Product p=productService.findById(1L);
       assertEquals(product,p);
       verify(productRepository).findById(1L);
    }

    @Test
    void findAll_WhenNotFound_ShouldThrowException() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(ApiException.class,()->productService.findAll());
        verify(productRepository).findAll();
    }

    @Test
    void findAll_WhenProductsFound_ShouldReturnList() {
        Product product=new Product();
        product.setId(1L);
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product>products=productService.findAll();
        assertEquals(1,products.size());
        assertEquals(product,products.get(0));
        verify(productRepository).findAll();
    }



    @Test
    void save() {
        Product product=new Product();
        product.setId(1L);
        productService.save(product);
        verify(productRepository).save(product);

    }

    @Test
    void findByCategory_WhenNoProductsFound_ShouldThrowException() {
        long categoryId = 1L;

        when(productRepository.findByCategory(categoryId)).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> productService.findByCategory(categoryId));
        assertEquals("No products found for category ID " + categoryId, exception.getMessage());
        verify(productRepository).findByCategory(categoryId);
    }

    @Test
    void findByCategory_WhenProductsFound_ShouldReturnList() {
        long categoryId = 1L;
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findByCategory(categoryId)).thenReturn(List.of(product));

        List<Product> products = productService.findByCategory(categoryId);

        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
        verify(productRepository).findByCategory(categoryId);
    }



    @Test
    void save_WhenProductAlreadyExists_ShouldThrowException() {
        Product product = new Product();
        product.setName("Existing Product");

        when(productRepository.findByProductName("Existing Product")).thenReturn(product);

        ApiException exception = assertThrows(ApiException.class, () -> productService.save(product));
        assertEquals("Product already exists", exception.getMessage());
        verify(productRepository).findByProductName("Existing Product");
    }

    @Test
    void delete_WhenProductExists_ShouldDeleteProduct() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void update_WhenProductExists_ShouldUpdateProduct() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Name");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product result = productService.update(1L, updatedProduct);

        assertEquals("Updated Name", existingProduct.getName());
        verify(productRepository).findById(1L);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void decreaseQuantity_WhenStockIsSufficient_ShouldDecreaseQuantity() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.decreaseQuantity(1L, 5);

        assertEquals(5, result.getStockQuantity());
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void decreaseQuantity_WhenStockIsInsufficient_ShouldThrowException() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ApiException exception = assertThrows(ApiException.class, () -> productService.decreaseQuantity(1L, 10));
        assertEquals("Insufficient stock. Decrease amount exceeds available quantity.", exception.getMessage());
        verify(productRepository).findById(1L);
    }

    @Test
    void increaseQuantity_ShouldIncreaseQuantity() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.increaseQuantity(1L, 5);

        assertEquals(15, result.getStockQuantity());
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }
}