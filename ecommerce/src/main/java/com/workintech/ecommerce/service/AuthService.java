package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Role;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.RoleRepository;
import com.workintech.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);

        Role role = roleRepository.findByAuthority("USER").orElseThrow(() -> new ApiException("Role does not exist", HttpStatus.NOT_FOUND));

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setAuthorities(Collections.singleton(role));

        return userRepository.save(user);
    }


}
