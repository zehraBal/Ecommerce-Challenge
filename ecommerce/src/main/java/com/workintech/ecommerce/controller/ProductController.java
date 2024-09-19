package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.converter.ProductConverter;
import com.workintech.ecommerce.dto.ProductRequest;
import com.workintech.ecommerce.dto.ProductResponse;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.service.CategoryService;
import com.workintech.ecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/product")
public class ProductController {


    private ProductService productService;
    private CategoryService categoryService;

    @GetMapping
    public List<ProductResponse> getAll(){
        return ProductConverter.convertToProductResponseList(productService.findAll());
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable long id){
        return ProductConverter.convertToProductResponse(productService.findById(id));
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponse> getByCategory(@PathVariable long categoryId){
        return ProductConverter.convertToProductResponseList(productService.findByCategory(categoryId));
    }

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest productRequest){
        Product product=new Product();
        Category category=categoryService.findById(productRequest.category_id());
        product.setName(productRequest.name());
        product.setPrice(productRequest.price());
        product.setStockQuantity(productRequest.stockQuantity());
        product.setCategory(category);
        return ProductConverter.convertToProductResponse(productService.save(product));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable long id,@RequestBody Product product){
        return ProductConverter.convertToProductResponse(productService.update(id,product));
    }

    @PutMapping("/{id}/decreaseQuantity")
    public ProductResponse decreaseQuantity(@PathVariable long id,@RequestParam int decNum){
        return ProductConverter.convertToProductResponse(productService.decreaseQuantity(id,decNum));
    }

    @PutMapping("/{id}/increaseQuantity")
    public ProductResponse increaseQuantity(@PathVariable long id,@RequestParam int incNum){
        return ProductConverter.convertToProductResponse(productService.decreaseQuantity(id,incNum));
    }

    @DeleteMapping("/{id}")
    public ProductResponse delete(@PathVariable long id){
        return ProductConverter.convertToProductResponse(productService.delete(id));
    }
}
