package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.CategoryResponse;
import com.workintech.ecommerce.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(long id);
    Category save(Category category);
    Category update(long id,Category category);
    Category delete(long id);
}
