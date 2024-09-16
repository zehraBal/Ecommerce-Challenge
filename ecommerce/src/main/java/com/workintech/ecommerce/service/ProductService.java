package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    Product findById(long id);
    List<Product> findAll();
    List<Product> findByCategory(long categoryId);
    Product save(Product product);
    Product delete(long id);
    Product update(long id,Product product);
    Product decreaseQuantity(long id,int decNum);
    Product increaseQuantity(long id , int incNum);

}
