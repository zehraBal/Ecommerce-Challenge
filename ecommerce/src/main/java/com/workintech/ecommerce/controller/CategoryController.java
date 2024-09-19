package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.converter.CategoryConverter;
import com.workintech.ecommerce.dto.CategoryRequest;
import com.workintech.ecommerce.dto.CategoryResponse;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse>  getAll(){
        return CategoryConverter.convertToCategoryResponseList(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable long id){
        return CategoryConverter.convertToCategoryResponse(categoryService.findById(id));
    }

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest categoryRequest){
        Category category=new Category();
        category.setName(categoryRequest.name());
        return CategoryConverter.convertToCategoryResponse(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable long id,@RequestBody Category category){
        return CategoryConverter.convertToCategoryResponse(categoryService.update(id,category));
    }

    @DeleteMapping("/{id}")
    public CategoryResponse delete(@PathVariable long id){
        return CategoryConverter.convertToCategoryResponse(categoryService.delete(id));
    }
}
