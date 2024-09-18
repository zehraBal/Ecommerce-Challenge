package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.UserResponse;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.entity.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    public static UserResponse convertToUserResponse(User user) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                shoppingCart != null ? ShoppingCartConverter.convertToCartResponse(shoppingCart) : null
        );
    }

    public static List<UserResponse> convertToUserResponseList(List<User> users) {
        List<UserResponse> userResponses = new ArrayList<>();

        for (User user : users) {
            userResponses.add(convertToUserResponse(user));
        }

        return userResponses;
    }
}
