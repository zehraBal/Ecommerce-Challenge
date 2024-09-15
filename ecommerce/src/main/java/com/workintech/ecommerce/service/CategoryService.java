package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> findAll();
    CategoryResponse findById(long id);
}
