package com.workintech.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.ecommerce.dto.OrderRequest;
import com.workintech.ecommerce.dto.OrderResponse;
import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.PaymentDetails;
import com.workintech.ecommerce.entity.PaymentType;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.service.OrderService;
import com.workintech.ecommerce.service.ShoppingCartService;
import com.workintech.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private UserService userService;

    private Order order;
    private User user;
    private PaymentDetails paymentDetails;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("johnDoe");

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setTotal(150.0);

        paymentDetails = new PaymentDetails();
        paymentDetails.setId(1L);
        paymentDetails.setPaymentType(PaymentType.CASH);
    }

    @Test
    void getAll_ShouldReturnListOfOrders() throws Exception {
        when(orderService.findAll()).thenReturn(List.of(order));

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("johnDoe"))
                .andExpect(jsonPath("$[0].totalPrice").value(150.0));

        verify(orderService).findAll();
    }

    @Test
    void getById_ShouldReturnOrderById() throws Exception {
        when(orderService.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("johnDoe"))
                .andExpect(jsonPath("$.totalPrice").value(150.0));

        verify(orderService).findById(1L);
    }

    @Test
    void getByUser_ShouldReturnOrderByUserId() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        when(orderService.findByUser(user)).thenReturn(order);

        mockMvc.perform(get("/order/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("johnDoe"))
                .andExpect(jsonPath("$.totalPrice").value(150.0));

        verify(userService).findById(1L);
        verify(orderService).findByUser(user);
    }

    @Test
    void createOrderFromCart_ShouldCreateOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest(1L, paymentDetails);
        when(orderService.createOrderFromCart(orderRequest.getShoppingCartId(), orderRequest.getPaymentDetails())).thenReturn(order);

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("johnDoe"))
                .andExpect(jsonPath("$.totalPrice").value(150.0));

        verify(orderService).createOrderFromCart(1L, paymentDetails);
    }

    @Test
    void save_ShouldSaveOrder() throws Exception {
        when(orderService.save(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("johnDoe"))
                .andExpect(jsonPath("$.totalPrice").value(150.0));

        verify(orderService).save(any(Order.class));
    }

    @Test
    void update_ShouldUpdateOrder() throws Exception {
        when(orderService.update(anyLong(), any(Order.class))).thenReturn(order);

        mockMvc.perform(put("/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("johnDoe"))
                .andExpect(jsonPath("$.totalPrice").value(150.0));

        verify(orderService).update(eq(1L), any(Order.class));
    }

    @Test
    void delete_ShouldDeleteOrder() throws Exception {
        when(orderService.delete(1L)).thenReturn(order);

        mockMvc.perform(delete("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("johnDoe"))
                .andExpect(jsonPath("$.totalPrice").value(150.0));

        verify(orderService).delete(1L);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
