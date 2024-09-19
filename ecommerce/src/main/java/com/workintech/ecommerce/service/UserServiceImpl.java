package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Role;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        List<User> users=userRepository.findAll();
        if (users.isEmpty()) {
            throw new ApiException("No users found", HttpStatus.NOT_FOUND);
        }
        return users;
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public User findByEmail(String email) {
        User user=userRepository.findByEmail(email);
        if(user==null){
           throw  new ApiException("User not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        User user=userRepository.findByPhoneNumber(phoneNumber);
        if(user==null){
            throw  new ApiException("User not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new ApiException("User cannot be null", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new ApiException("Username already exists", HttpStatus.CONFLICT);
        }
//        if (user.getPassword().length() < 8) {
//            throw new IllegalArgumentException("Password must be at least 8 characters long");
//        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            Role defaultRole = new Role();
            defaultRole.setAuthority("USER"); // Default rol "USER"
            user.setAuthorities(Collections.singleton(defaultRole));
        }

        return userRepository.save(user);
    }

    @Override
    public User update(long id, User user) {
        User userToUpdate = findById(id);

        if (user.getUsername() != null) userToUpdate.setUsername(user.getUsername());
        if (user.getPassword() != null) userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getFirstName() != null) userToUpdate.setFirstName(user.getFirstName());
        if (user.getLastName() != null) userToUpdate.setLastName(user.getLastName());
        if (user.getEmail() != null) userToUpdate.setEmail(user.getEmail());
        if (user.getPhoneNumber() != null) userToUpdate.setPhoneNumber(user.getPhoneNumber());
        if (user.getShoppingCart() != null) userToUpdate.setShoppingCart(user.getShoppingCart());
        if (user.getOrders() != null) userToUpdate.setOrders(user.getOrders());
        if (user.getAuthorities() != null) {
            Set<Role> roles = new HashSet<>();
            user.getAuthorities().forEach(authority -> {
                if (authority instanceof Role) {
                    roles.add((Role) authority);
                }
            });
            userToUpdate.setAuthorities(roles);
        }

        return userRepository.save(userToUpdate);
    }

    @Override
    public User delete(long id) {
        User userToDelete = findById(id);
        userRepository.delete(userToDelete);
        return userToDelete;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("User credentials are not valid");
                    throw new UsernameNotFoundException("User credentials are not valid");
                });
    }
}
