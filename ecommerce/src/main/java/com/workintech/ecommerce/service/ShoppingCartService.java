package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.ShoppingCartResponse;
import com.workintech.ecommerce.entity.Product;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCartResponse addProductToCart(long id,Product product);
    ShoppingCartResponse removeProductFromCart(long id,Product product);
    ShoppingCartResponse findShoppingCartById(long id);
    List<ShoppingCartResponse> findAll();
}
