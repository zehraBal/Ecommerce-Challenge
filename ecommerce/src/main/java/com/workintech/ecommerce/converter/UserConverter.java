package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.UserResponse;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.entity.ShoppingCart;

public class UserConverter {
    public static UserResponse convertToUserResponse(User user) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                shoppingCart != null ? ShoppingCartConverter.convertToShoppingCartResponse(shoppingCart) : null
        );
    }
}
