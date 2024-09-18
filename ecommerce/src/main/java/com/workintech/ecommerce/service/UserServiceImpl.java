package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(()->new ApiException("User not found",HttpStatus.NOT_FOUND));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User findByUsername(String username){return userRepository.findByUsername(username);}

    @Override
    public User save(User user) {
        if (user == null) {
            throw new ApiException("User cannot be null", HttpStatus.BAD_REQUEST);
        }
      return userRepository.save(user);
    }

    @Override
    public User update(long id,User user) {
      User userToUpdate = findById(id);
      userToUpdate.setUsername(user.getUsername());
      userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
      userToUpdate.setFirstName(user.getFirstName());
      userToUpdate.setLastName(user.getLastName());
      userToUpdate.setEmail(user.getEmail());
      userToUpdate.setPhoneNumber(user.getPhoneNumber());
      userToUpdate.setShoppingCart(user.getShoppingCart());
      userToUpdate.setOrders(user.getOrders());
      userToUpdate.setCreatedAt(user.getCreatedAt());
     return userRepository.save(userToUpdate);
    }

    @Override
    public User delete(long id) {
        User userToDelete = findById(id);
        userRepository.delete(userToDelete);
        return userToDelete;
    }


//    @Override
//    public User updateEmail(long id, String email) {
//        User userToUpdate = findById(id);
//        userToUpdate.setEmail(email);
//       return userRepository.save(userToUpdate);
//    }

//    @Override
//    public User updateFirstName(long id, String firstName) {
//        User userToUpdate = findById(id);
//        userToUpdate.setFirstName(firstName);
//      return  userRepository.save(userToUpdate);
//    }
//
//    @Override
//    public User updateLastName(long id, String lastName) {
//        User userToUpdate = findById(id);
//        userToUpdate.setLastName(lastName);
//       return userRepository.save(userToUpdate);
//    }
//
//    @Override
//    public User updatePhoneNumber(long id, String phoneNumber) {
//        User userToUpdate = findById(id);
//        userToUpdate.setPhoneNumber(phoneNumber);
//       return userRepository.save(userToUpdate);
//    }
//
//    @Override
//    public User updatePassword(long id, String password) {
//        User userToUpdate = findById(id);
//        userToUpdate.setPassword(passwordEncoder.encode(password));
//        return userRepository.save(userToUpdate);
//    }


}
