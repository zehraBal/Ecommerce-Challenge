package com.workintech.ecommerce.dto;

public record ProductRequest(String name,int stockQuantity,double price,long category_id) {
}
