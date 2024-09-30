package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.converter.UserConverter;
import com.workintech.ecommerce.dto.LoginRequest;
import com.workintech.ecommerce.dto.UserResponse;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/user")
class UserController {

    private  UserService userService;

    @GetMapping
    public List<UserResponse>  getAll(){
        return UserConverter.convertToUserResponseList(userService.findAll());
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable long id){
        return UserConverter.convertToUserResponse(userService.findById(id));
    }

    @GetMapping("/username/{username}")
    public UserResponse geyByUsername(@PathVariable String username){
       return UserConverter.convertToUserResponse(userService.findByUsername(username));
  }

    @GetMapping("/email/{email}")
    public UserResponse getByEmail(@PathVariable String email){
        return UserConverter.convertToUserResponse(userService.findByEmail(email));
    }

    @GetMapping("/phone/{phoneNumber}")
    public UserResponse getByPhoneNumber(@PathVariable String phoneNumber){
        return UserConverter.convertToUserResponse(userService.findByPhoneNumber(phoneNumber));
    }

    @PostMapping
    public UserResponse save(@RequestBody User user){
        return UserConverter.convertToUserResponse(userService.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean isValidUser = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (isValidUser) {
            return ResponseEntity.ok("Giriş başarılı!");
        } else {
            return ResponseEntity.status(401).body("Geçersiz kullanıcı adı veya şifre.");
        }
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable long id,@RequestBody User user){
        return UserConverter.convertToUserResponse(userService.update(id,user));
    }

    @DeleteMapping("/{id}")
    public UserResponse delete(@PathVariable long id){
        return UserConverter.convertToUserResponse(userService.delete(id));
    }
}
