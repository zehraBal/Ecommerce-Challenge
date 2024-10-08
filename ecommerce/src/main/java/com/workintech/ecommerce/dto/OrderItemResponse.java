package com.workintech.ecommerce.dto;

public record OrderItemResponse(Long id, String productName,int quantity,double price,double total) {
    public double getTotal() {
        return price * quantity;
    }
}