package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id).orElseThrow(()->new ApiException("Category not found.", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(long id, Category category) {
        Category categorytoUpdate=findById(id);
        categorytoUpdate.setName(category.getName());
        categorytoUpdate.setProducts(category.getProducts());
        return categoryRepository.save(categorytoUpdate);
    }

    @Override
    @Transactional
    public Category delete(long id) {
        Category category=findById(id);
        categoryRepository.delete(category);
        return category;
    }
}
