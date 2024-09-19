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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Category not found.", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Category save(Category category) {
        if (category == null) {
            throw new ApiException("Category cannot be null", HttpStatus.BAD_REQUEST);
        }
      if(categoryRepository.findByName(category.getName())!=null){
          throw new ApiException("Category with the name '" + category.getName() + "' already exists.", HttpStatus.CONFLICT);

      }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(long id, Category category) {
        Category categoryToUpdate = findById(id);
        if (categoryToUpdate == null) {
            throw new ApiException("Category not found for update.", HttpStatus.NOT_FOUND);
        }
        categoryToUpdate.setName(category.getName());
        categoryToUpdate.setProducts(category.getProducts());
        return categoryRepository.save(categoryToUpdate);
    }

    @Override
    @Transactional
    public Category delete(long id) {
        Category category = findById(id);
        if (category == null) {
            throw new ApiException("Category not found for deletion.", HttpStatus.NOT_FOUND);
        }
        categoryRepository.delete(category);
        return category;
    }
}
