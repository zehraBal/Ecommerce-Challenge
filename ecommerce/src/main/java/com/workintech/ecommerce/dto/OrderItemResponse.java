package com.workintech.ecommerce.dto;

public record OrderItemResponse(Long productId, String productName,int quantity,double price,double total) {
    public double getTotal() {
        return price * quantity;
    }
}