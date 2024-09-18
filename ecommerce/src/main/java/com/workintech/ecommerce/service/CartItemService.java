package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;

import java.util.List;

public interface CartItemService {
   CartItem  findByProductId(long productId);
   List<CartItem> findByShoppingCart(long cartId);
   CartItem findById(long id);
   List<CartItem> findAll();
   CartItem save(CartItem cartItem);
   CartItem delete(long id);
   CartItem update(long id,CartItem cartItem);
   CartItem addProductToCart(ShoppingCart shoppingCart, Product product, int quantity);
   void removeProductFromCart(ShoppingCart shoppingCart, Product product,int quantity);
   List<CartItem> getItemsInCart(ShoppingCart shoppingCart);
   void clearCart(ShoppingCart shoppingCart);

}
