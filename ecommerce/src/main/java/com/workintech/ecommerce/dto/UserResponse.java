package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.ShoppingCart;

public record UserResponse(Long id,String username, ShoppingCartResponse shoppingCartResponse) {
}
