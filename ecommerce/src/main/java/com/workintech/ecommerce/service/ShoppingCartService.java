package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.entity.User;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCart addProductToCart(long id,long productId,int quantity);
    ShoppingCart removeProductFromCart(long id,long productId,int quantity);
    ShoppingCart findById(long id);
   // ShoppingCart findByUser(User user);
    List<ShoppingCart> findAll();
    ShoppingCart createNewShoppingCart(User user);
    ShoppingCart delete(long id);
    ShoppingCart update(long id,ShoppingCart shoppingCart);
    ShoppingCart save(ShoppingCart shoppingCart);
    ShoppingCart clearCart(long id);
    double calculateTotalPrice(long id);
}
