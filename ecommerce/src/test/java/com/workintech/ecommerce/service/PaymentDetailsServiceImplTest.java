package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.PaymentDetails;
import com.workintech.ecommerce.entity.PaymentType;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.OrderRepository;
import com.workintech.ecommerce.repository.PaymentDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PaymentDetailsServiceImplTest {

    private PaymentDetailsService paymentDetailsService;

    @Mock
    private PaymentDetailsRepository paymentDetailsRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        paymentDetailsService = new PaymentDetailsServiceImpl(paymentDetailsRepository, orderRepository);
    }

    @Test
    void findAll_WhenPaymentDetailsFound_ShouldReturnList() {
        PaymentDetails paymentDetail = new PaymentDetails();
        when(paymentDetailsRepository.findAll()).thenReturn(List.of(paymentDetail));

        List<PaymentDetails> result = paymentDetailsService.findAll();

        assertEquals(1, result.size());
        assertEquals(paymentDetail, result.get(0));
        verify(paymentDetailsRepository).findAll();
    }

    @Test
    void findAll_WhenNoPaymentDetailsFound_ShouldThrowException() {
        when(paymentDetailsRepository.findAll()).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> paymentDetailsService.findAll());
        assertEquals("No payment detail found", exception.getMessage());
        verify(paymentDetailsRepository).findAll();
    }

    @Test
    void findById_WhenPaymentDetailExists_ShouldReturnPaymentDetail() {
        PaymentDetails paymentDetail = new PaymentDetails();
        paymentDetail.setId(1L);

        when(paymentDetailsRepository.findById(1L)).thenReturn(Optional.of(paymentDetail));

        PaymentDetails result = paymentDetailsService.findById(1L);

        assertEquals(paymentDetail, result);
        verify(paymentDetailsRepository).findById(1L);
    }

    @Test
    void findById_WhenPaymentDetailNotFound_ShouldThrowException() {
        when(paymentDetailsRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> paymentDetailsService.findById(1L));
        assertEquals("Payment detail not found.", exception.getMessage());
        verify(paymentDetailsRepository).findById(1L);
    }

    @Test
    void findByOrderId_WhenOrderExistsAndPaymentDetailExists_ShouldReturnPaymentDetail() {
        Order order = new Order();
        order.setId(1L);
        PaymentDetails paymentDetail = new PaymentDetails();
        paymentDetail.setOrder(order);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentDetailsRepository.findByOrderId(1L)).thenReturn(paymentDetail);

        PaymentDetails result = paymentDetailsService.findByOrderId(1L);

        assertEquals(paymentDetail, result);
        verify(orderRepository).findById(1L);
        verify(paymentDetailsRepository).findByOrderId(1L);
    }

    @Test
    void findByOrderId_WhenOrderNotFound_ShouldThrowException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> paymentDetailsService.findByOrderId(1L));
        assertEquals("Order not found.", exception.getMessage());
        verify(orderRepository).findById(1L);
    }

    @Test
    void findByOrderId_WhenPaymentDetailNotFound_ShouldThrowException() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentDetailsRepository.findByOrderId(1L)).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> paymentDetailsService.findByOrderId(1L));
        assertEquals("Payment detail not found for order ID 1", exception.getMessage());
        verify(orderRepository).findById(1L);
        verify(paymentDetailsRepository).findByOrderId(1L);
    }

    @Test
    void delete_WhenPaymentDetailExists_ShouldDeletePaymentDetail() {
        PaymentDetails paymentDetail = new PaymentDetails();
        paymentDetail.setId(1L);

        when(paymentDetailsRepository.findById(1L)).thenReturn(Optional.of(paymentDetail));

        PaymentDetails result = paymentDetailsService.delete(1L);

        assertEquals(paymentDetail, result);
        verify(paymentDetailsRepository).findById(1L);
        verify(paymentDetailsRepository).delete(paymentDetail);
    }

    @Test
    void save_WhenPaymentDetailIsNew_ShouldSavePaymentDetail() {
        Order order = new Order();
        order.setId(1L);
        PaymentDetails paymentDetail = new PaymentDetails();
        paymentDetail.setOrder(order);

        when(paymentDetailsRepository.findByOrderId(1L)).thenReturn(null);
        when(paymentDetailsRepository.save(paymentDetail)).thenReturn(paymentDetail);

        PaymentDetails result = paymentDetailsService.save(paymentDetail);

        assertEquals(paymentDetail, result);
        verify(paymentDetailsRepository).findByOrderId(1L);
        verify(paymentDetailsRepository).save(paymentDetail);
    }

    @Test
    void save_WhenPaymentDetailAlreadyExists_ShouldThrowException() {
        Order order = new Order();
        order.setId(1L);
        PaymentDetails paymentDetail = new PaymentDetails();
        paymentDetail.setOrder(order);

        when(paymentDetailsRepository.findByOrderId(1L)).thenReturn(paymentDetail);

        ApiException exception = assertThrows(ApiException.class, () -> paymentDetailsService.save(paymentDetail));
        assertEquals("Payment detail already exists", exception.getMessage());
        verify(paymentDetailsRepository).findByOrderId(1L);
    }

    @Test
    void update_WhenPaymentDetailExists_ShouldUpdatePaymentDetail() {
        PaymentDetails existingPaymentDetail = new PaymentDetails();
        existingPaymentDetail.setId(1L);

        PaymentDetails updatedPaymentDetail = new PaymentDetails();
        updatedPaymentDetail.setPaymentType(PaymentType.CASH);

        when(paymentDetailsRepository.findById(1L)).thenReturn(Optional.of(existingPaymentDetail));
        when(paymentDetailsRepository.save(existingPaymentDetail)).thenReturn(existingPaymentDetail);

        PaymentDetails result = paymentDetailsService.update(1L, updatedPaymentDetail);

        assertEquals(PaymentType.CASH, existingPaymentDetail.getPaymentType());
        verify(paymentDetailsRepository).findById(1L);
        verify(paymentDetailsRepository).save(existingPaymentDetail);
    }
}
