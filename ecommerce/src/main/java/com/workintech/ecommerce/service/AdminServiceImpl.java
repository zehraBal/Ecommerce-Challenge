package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.CategoryRepository;
import com.workintech.ecommerce.repository.ProductRepository;
import com.workintech.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;


    @Override
    public User createUser(User user) {
        //user null olamaz
      return userRepository.save(user);
    }

    @Override
    public User updateUser(long id,User user) {
      User userToUpdate = userRepository.findById(id).orElseThrow(()->new ApiException("There is no user associated with this id.", HttpStatus.NOT_FOUND));
      userToUpdate.setUsername(user.getUsername());
      userToUpdate.setPassword(user.getPassword());
      userToUpdate.setFirstName(user.getFirstName());
      userToUpdate.setLastName(user.getLastName());
      userToUpdate.setEmail(user.getEmail());
      userToUpdate.setPhoneNumber(user.getPhoneNumber());
      userToUpdate.setShoppingCart(user.getShoppingCart());
      userToUpdate.setOrders(user.getOrders());
      userToUpdate.setCreatedAt(user.getCreatedAt());
      userRepository.save(userToUpdate);
      return userToUpdate;
    }

    @Override
    public User deleteUser(long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(()->new ApiException("There is no user associated with this id.", HttpStatus.NOT_FOUND));
        userRepository.delete(userToDelete);
        return userToDelete;
    }

    @Override
    public Product createProduct(Product product) {
       return productRepository.save(product);
    }

    @Override
    public Product deleteProduct(long id) {
        Product productToDelete=productRepository.findById(id).orElseThrow(()->new ApiException("There is no product associated with this id.", HttpStatus.NOT_FOUND));
        productRepository.delete(productToDelete);
        return productToDelete;
    }

    @Override
    public Product updateProduct(long id,Product product) {
        Product productToUpdate=productRepository.findById(id).orElseThrow(()->new ApiException("There is no product associated with this id.", HttpStatus.NOT_FOUND));
        productToUpdate.setName(product.getName());
        productToUpdate.setStockQuantity(product.getStockQuantity());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setShoppingCarts(product.getShoppingCarts());
        productToUpdate.setCreatedAt(product.getCreatedAt());
        productRepository.save(productToUpdate);
        return productToUpdate;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category deleteCategory(Long id) {
        Category categoryToDelete=categoryRepository.findById(id).orElseThrow(()->new ApiException("There is no category associated with this id.", HttpStatus.NOT_FOUND));
        categoryRepository.delete(categoryToDelete);
        return categoryToDelete;
    }

    @Override
    public Category updateCategory(Long id,Category category) {
        Category categoryToUpdate=categoryRepository.findById(id).orElseThrow(()->new ApiException("There is no category associated with this id.", HttpStatus.NOT_FOUND));
        categoryToUpdate.setName(category.getName());
        categoryToUpdate.setProducts(category.getProducts());
        categoryToUpdate.setCreatedAt(category.getCreatedAt());
        categoryRepository.save(categoryToUpdate);
        return categoryToUpdate;
    }
}
