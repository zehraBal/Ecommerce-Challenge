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
        List<Product> products= productRepository.findAll();
        if (products.isEmpty()) {
            throw new ApiException("No products found", HttpStatus.NOT_FOUND);
        }
        return products;
    }

    @Override
    public List<Product> findByCategory(long categoryId) {
       List<Product> products = productRepository.findByCategory(categoryId);
       if(products==null || products.isEmpty()){
           throw new ApiException("No products found for category ID " + categoryId, HttpStatus.NOT_FOUND);
       }
       return products;
    }

    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new ApiException("Product cannot be null", HttpStatus.BAD_REQUEST);
        }
        if(productRepository.findByProductName(product.getName())!=null){
            throw new ApiException("Product already exists", HttpStatus.CONFLICT);
        }
        return productRepository.save(product);
    }

    @Override
    public Product delete(long id) {
        Product product=productRepository.findById(id).orElseThrow(()->new ApiException("Product not found", HttpStatus.NOT_FOUND));
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
        if(decNum<=0){
            throw new ApiException("Decrease number cannot be less than 0. ", HttpStatus.BAD_REQUEST);
        }
        Product product=findById(id);
        int quantity=product.getStockQuantity()-decNum;
        if(quantity<1){
            throw new ApiException("Insufficient stock. Decrease amount exceeds available quantity.", HttpStatus.BAD_REQUEST);

        }
        product.setStockQuantity(quantity);
        return productRepository.save(product);
    }

    @Override
    public Product increaseQuantity(long id, int incNum) {
        if(incNum<=0){
            throw new ApiException("Increase number cannot be less than 0. ", HttpStatus.BAD_REQUEST);
        }
        Product product=findById(id);
        int quantity=product.getStockQuantity()+incNum;
        product.setStockQuantity(quantity);
        return productRepository.save(product);
    }
}
