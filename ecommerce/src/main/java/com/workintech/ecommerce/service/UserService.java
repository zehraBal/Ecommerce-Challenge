package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(long id);
    User findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
//    User findByUsername(String username);
//    User updateEmail(long id,String email);
//    User updateFirstName(long id,String firstName);
//    User updateLastName(long id,String lastName);
//    User updatePhoneNumber(long id,String phoneNumber);
//    User updatePassword(long id,String password);
    User save(User user);
    User update(long id,User user);
    User delete(long id);
}
