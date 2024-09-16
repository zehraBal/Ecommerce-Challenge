package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    @Override
    public Product findById(long id) {
     return productRepository.findById(id).orElseThrow(()->new ApiException("Product not found", HttpStatus.NOT_FOUND));

    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByCategory(long categoryId) {
        return productRepository.findByCategory(categoryId);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product delete(long id) {
        Product product=findById(id);
        productRepository.deleteById(id);
        return product;
    }

    @Override
    public Product update(long id, Product product) {
        Product productToUpdate=findById(id);
        productToUpdate.setName(product.getName());
        productToUpdate.setStockQuantity(product.getStockQuantity());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setShoppingCarts(product.getShoppingCarts());
        productToUpdate.setCreatedAt(product.getCreatedAt());
        productRepository.save(productToUpdate);
        return productToUpdate;
    }

    @Override
    public Product decreaseQuantity(long id, int decNum) {
        Product product=findById(id);
        int quantity=product.getStockQuantity()-decNum;
        product.setStockQuantity(quantity);
        return productRepository.save(product);
    }

    @Override
    public Product increaseQuantity(long id, int incNum) {
        Product product=findById(id);
        int quantity=product.getStockQuantity()+incNum;
        product.setStockQuantity(quantity);
        return productRepository.save(product);
    }
}
