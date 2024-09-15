package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.Category;

public record ProductResponse(Long id, String name, int stockQuantity, double price,String  categoryName) {
}
