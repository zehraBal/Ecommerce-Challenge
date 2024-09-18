package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.CartItemResponse;
import com.workintech.ecommerce.dto.ShoppingCartResponse;
import com.workintech.ecommerce.entity.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartConverter {

    public static ShoppingCartResponse convertToCartResponse(ShoppingCart shoppingCart) {
        List<CartItemResponse> cartItems=CartItemConverter.convertToCartItemResponseList(shoppingCart.getItems());
        return new ShoppingCartResponse(shoppingCart.getId(),shoppingCart.getUser().getUsername(),cartItems);

    }

    public static List<ShoppingCartResponse> convertToCartResponseList(List<ShoppingCart> carts){
        List<ShoppingCartResponse> cartResponses=new ArrayList<>();
        for(ShoppingCart c:carts){
            cartResponses.add(convertToCartResponse(c));
        }
        return cartResponses;
    }
}
