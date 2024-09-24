package com.workintech.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.ecommerce.dto.PaymentDetailResponse;
import com.workintech.ecommerce.entity.PaymentDetails;
import com.workintech.ecommerce.entity.PaymentType;
import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.service.PaymentDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentDetailsService paymentService;

    private PaymentDetails paymentDetails;
    private User user;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("janeD");

        order = new Order();
        order.setId(1L);
        order.setUser(user);

        paymentDetails = new PaymentDetails();
        paymentDetails.setId(1L);
        paymentDetails.setPaymentType(PaymentType.CASH);
        paymentDetails.setOrder(order);
        paymentDetails.setUser(user);
        paymentDetails.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void getAll_ShouldReturnListOfPaymentDetails() throws Exception {
        when(paymentService.findAll()).thenReturn(List.of(paymentDetails));

        mockMvc.perform(get("/payment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("janeD"))
                .andExpect(jsonPath("$[0].paymentType").value("CASH"))
                .andExpect(jsonPath("$[0].oderId").value(1L));

        verify(paymentService).findAll();
    }

    @Test
    void getById_ShouldReturnPaymentDetailById() throws Exception {
        when(paymentService.findById(1L)).thenReturn(paymentDetails);

        mockMvc.perform(get("/payment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("janeD"))
                .andExpect(jsonPath("$.paymentType").value("CASH"))
                .andExpect(jsonPath("$.oderId").value(1L));

        verify(paymentService).findById(1L);
    }

    @Test
    void getByOrderId_ShouldReturnPaymentDetailByOrderId() throws Exception {
        when(paymentService.findByOrderId(1L)).thenReturn(paymentDetails);

        mockMvc.perform(get("/payment/byOrder/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("janeD"))
                .andExpect(jsonPath("$.paymentType").value("CASH"))
                .andExpect(jsonPath("$.oderId").value(1L));

        verify(paymentService).findByOrderId(1L);
    }

    @Test
    void save_ShouldCreateNewPaymentDetail() throws Exception {
        when(paymentService.save(any(PaymentDetails.class))).thenReturn(paymentDetails);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(paymentDetails))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("janeD"))
                .andExpect(jsonPath("$.paymentType").value("CASH"))
                .andExpect(jsonPath("$.oderId").value(1L));

        verify(paymentService).save(any(PaymentDetails.class));
    }

    @Test
    void update_ShouldUpdatePaymentDetail() throws Exception {
        when(paymentService.update(anyLong(), any(PaymentDetails.class))).thenReturn(paymentDetails);

        mockMvc.perform(put("/payment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(paymentDetails))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("janeD"))
                .andExpect(jsonPath("$.paymentType").value("CASH"))
                .andExpect(jsonPath("$.oderId").value(1L));

        verify(paymentService).update(eq(1L), any(PaymentDetails.class));
    }

    @Test
    void delete_ShouldDeletePaymentDetail() throws Exception {
        when(paymentService.delete(1L)).thenReturn(paymentDetails);

        mockMvc.perform(delete("/payment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("janeD"))
                .andExpect(jsonPath("$.paymentType").value("CASH"))
                .andExpect(jsonPath("$.oderId").value(1L));

        verify(paymentService).delete(1L);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
