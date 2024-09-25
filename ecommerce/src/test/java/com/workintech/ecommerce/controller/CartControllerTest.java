package com.workintech.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.ecommerce.config.TestSecurityConfig;
import com.workintech.ecommerce.dto.CartItemResponse;
import com.workintech.ecommerce.dto.ShoppingCartResponse;
import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.service.CartItemService;
import com.workintech.ecommerce.service.ProductService;
import com.workintech.ecommerce.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@Import(TestSecurityConfig.class)  // Security Configuration
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private CartItemService cartItemService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShoppingCart shoppingCart;
    private User user;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("johnDoe");

        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setTotalPrice(100.0);
        user.setShoppingCart(shoppingCart);
        shoppingCart.setUser(user);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAll_ShouldReturnShoppingCarts() throws Exception {
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse(1L, "johndoe", Collections.emptyList());
        when(shoppingCartService.findAll()).thenReturn(List.of(shoppingCart));

        mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(shoppingCartService).findAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_ShouldReturnShoppingCartById() throws Exception {
        when(shoppingCartService.findById(1L)).thenReturn(shoppingCart);

        mockMvc.perform(get("/cart/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(shoppingCartService).findById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getItemsInCart_ShouldReturnItemsInCart() throws Exception {
        CartItemResponse cartItemResponse = new CartItemResponse(1L, "product", 1, 100.0, 100.0);
        when(shoppingCartService.findById(1L)).thenReturn(shoppingCart);
        when(cartItemService.getItemsInCart(shoppingCart)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cart/1/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(cartItemService).getItemsInCart(shoppingCart);
    }

    @Test
    @WithMockUser(roles = "USER")
    void save_ShouldCreateShoppingCart() throws Exception {
        when(shoppingCartService.save(any(ShoppingCart.class))).thenReturn(shoppingCart);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoppingCart)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(shoppingCartService).save(any(ShoppingCart.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void addProductToCart_ShouldAddProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        CartItem cartItem = new CartItem();  // CartItemResponse yerine CartItem
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPrice(100.0);

        when(shoppingCartService.findById(1L)).thenReturn(shoppingCart);
        when(productService.findById(1L)).thenReturn(product);
        when(cartItemService.addProductToCart(any(ShoppingCart.class), any(Product.class), eq(2)))
                .thenReturn(cartItem);  // CartItem döndürülmeli

        mockMvc.perform(post("/cart/1/add/1")
                        .param("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(cartItemService).addProductToCart(any(ShoppingCart.class), any(Product.class), eq(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    void removeProductFromCart_ShouldRemoveProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        CartItem cartItem = new CartItem();  // CartItemResponse yerine CartItem
        cartItem.setId(1L);
        cartItem.setProduct(product);

        when(shoppingCartService.findById(1L)).thenReturn(shoppingCart);
        when(productService.findById(1L)).thenReturn(product);
        when(cartItemService.findByProductId(1L)).thenReturn(cartItem);  // CartItem döndürülmeli

        mockMvc.perform(put("/cart/1/remove/1")
                        .param("quantity", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(cartItemService).removeProductFromCart(any(ShoppingCart.class), any(Product.class), eq(1));
    }


    @Test
    @WithMockUser(roles = "USER")
    void delete_ShouldDeleteCart() throws Exception {
        when(shoppingCartService.delete(1L)).thenReturn(shoppingCart);

        mockMvc.perform(delete("/cart/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(shoppingCartService).delete(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void clearCart_ShouldClearCart() throws Exception {
        when(shoppingCartService.findById(1L)).thenReturn(shoppingCart);

        mockMvc.perform(delete("/cart/1/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(cartItemService).clearCart(shoppingCart);
    }
}
