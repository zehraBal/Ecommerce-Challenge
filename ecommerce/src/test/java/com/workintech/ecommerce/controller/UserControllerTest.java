package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.converter.UserConverter;
import com.workintech.ecommerce.dto.UserResponse;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    void getAll_ShouldReturnListOfUsers() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");

        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john_doe"));

        verify(userService).findAll();
    }

    @Test
    void getById_ShouldReturnUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));

        verify(userService).findById(1L);
    }

    @Test
    void getByEmail_ShouldReturnUserByEmail() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john_doe@example.com");  // Bu alan User'da olacak, ancak DTO'da olmayacak.

        when(userService.findByEmail("john_doe@example.com")).thenReturn(user);

        mockMvc.perform(get("/user/email/john_doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));  // email yerine username kontrol ediliyor

        verify(userService).findByEmail("john_doe@example.com");
    }

    @Test
    void getByPhoneNumber_ShouldReturnUserByPhoneNumber() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setPhoneNumber("123456789");  // Bu alan User'da olacak, ancak DTO'da olmayacak.

        when(userService.findByPhoneNumber("123456789")).thenReturn(user);

        mockMvc.perform(get("/user/phone/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));  // phoneNumber yerine username kontrol ediliyor

        verify(userService).findByPhoneNumber("123456789");
    }

    @Test
    void save_ShouldCreateNewUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");

        when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));

        verify(userService).save(any(User.class));
    }

    @Test
    void update_ShouldUpdateExistingUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe_updated");

        when(userService.update(anyLong(), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe_updated\",\"password\":\"newpassword123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe_updated"));

        verify(userService).update(anyLong(), any(User.class));
    }

    @Test
    void delete_ShouldDeleteUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");

        when(userService.delete(1L)).thenReturn(user);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));

        verify(userService).delete(1L);
    }
}
