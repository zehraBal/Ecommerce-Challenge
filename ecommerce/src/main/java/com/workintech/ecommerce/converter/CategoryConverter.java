package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.CategoryResponse;
import com.workintech.ecommerce.dto.ProductResponse;
import com.workintech.ecommerce.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryConverter {

    public static CategoryResponse convertToCategoryResponse(Category category) {
        List<ProductResponse> products=ProductConverter.convertToProductResponseList(category.getProducts());
    return new CategoryResponse(category.getId(), category.getName(),products);
    }

    public static List<CategoryResponse> convertToCategoryResponseList(List<Category> categories){
        List<CategoryResponse> responses=new ArrayList<>();
        for(Category c:categories){
            responses.add(convertToCategoryResponse(c));
        }
        return responses;
    }
}
