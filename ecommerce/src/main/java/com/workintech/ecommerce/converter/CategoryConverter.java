package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.CategoryResponse;
import com.workintech.ecommerce.dto.ProductResponse;
import com.workintech.ecommerce.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryConverter {

    public static CategoryResponse convertToCategoryResponse(Category category) {
        List<ProductResponse> productResponses = category.getProducts().stream()
                .map(ProductConverter::convertToProductResponse)
                .collect(Collectors.toList());

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                productResponses
        );
    }
}
