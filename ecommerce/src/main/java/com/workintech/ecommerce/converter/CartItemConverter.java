package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.CartItemResponse;
import com.workintech.ecommerce.entity.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartItemConverter {

    public static CartItemResponse convertToCartItemResponse (CartItem item){

        return new CartItemResponse(item.getId(),item.getProduct().getName(), item.getQuantity(),item.getPrice(),(item.getQuantity())* item.getPrice());
    }

    public static List<CartItemResponse> convertToCartItemResponseList(List<CartItem> items){
        List<CartItemResponse> itemResponses=new ArrayList<>();
        for(CartItem i : items){
           itemResponses.add(convertToCartItemResponse(i));
        }
        return itemResponses;
    }
}
