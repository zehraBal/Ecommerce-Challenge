package com.workintech.ecommerce.dto;

import java.util.List;

public record ShoppingCartResponse(Long id,String username,List<CartItemResponse> cartItems) {
}
