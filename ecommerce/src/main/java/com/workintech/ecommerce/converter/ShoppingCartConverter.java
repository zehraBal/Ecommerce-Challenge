package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.ShoppingCartResponse;
import com.workintech.ecommerce.dto.ProductResponse;
import com.workintech.ecommerce.entity.ShoppingCart;

import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartConverter {

    public static ShoppingCartResponse convertToShoppingCartResponse(ShoppingCart shoppingCart) {
        List<ProductResponse> productResponses = shoppingCart.getProducts().stream()
                .map(ProductConverter::convertToProductResponse)
                .collect(Collectors.toList());

        return new ShoppingCartResponse(
                shoppingCart.getId(),
                productResponses
        );
    }
}
