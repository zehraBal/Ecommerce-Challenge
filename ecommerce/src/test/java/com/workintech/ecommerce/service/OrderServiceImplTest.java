package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.*;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private OrderService orderService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemService orderItemService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(shoppingCartService, orderRepository, orderItemService);
    }

    @Test
    void createOrderFromCart_WhenCartIsEmpty_ShouldThrowException() {
        long cartId = 1L;
        ShoppingCart emptyCart = new ShoppingCart();
        emptyCart.setItems(Collections.emptyList());  // Sepetin boş olduğu durum için items boş liste olmalı

        when(shoppingCartService.findById(cartId)).thenReturn(emptyCart);

        ApiException exception = assertThrows(ApiException.class, () -> orderService.createOrderFromCart(cartId, new PaymentDetails()));
        assertEquals("Shopping cart is empty or does not exist", exception.getMessage());
        verify(shoppingCartService).findById(cartId);
    }

    @Test
    void createOrderFromCart_WhenPaymentDetailsAreNull_ShouldThrowException() {
        long cartId = 1L;
        ShoppingCart cart = new ShoppingCart();
        cart.setItems(List.of(new CartItem()));

        when(shoppingCartService.findById(cartId)).thenReturn(cart);

        ApiException exception = assertThrows(ApiException.class, () -> orderService.createOrderFromCart(cartId, null));
        assertEquals("Payment details cannot be null", exception.getMessage());
        verify(shoppingCartService).findById(cartId);
    }

    @Test
    void createOrderFromCart_WhenValid_ShouldCreateOrder() {
        long cartId = 1L;

        // Sepet ve ürünleri ayarlıyoruz
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(new User());

        CartItem cartItem = new CartItem();
        cartItem.setPrice(100.0);
        cartItem.setQuantity(2);

        cart.setItems(List.of(cartItem));  // items dolu olmalı, yoksa total 0 olur

        PaymentDetails paymentDetails = new PaymentDetails();
        Order savedOrder = new Order();
        savedOrder.setId(1L);

        when(shoppingCartService.findById(cartId)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createOrderFromCart(cartId, paymentDetails);

        assertEquals(savedOrder, result);
        verify(orderRepository).save(any(Order.class));
        verify(orderItemService).createOrderItemsFromCart(cart.getItems(), savedOrder);
        verify(shoppingCartService).clearCart(cartId);
    }

    @Test
    void findById_WhenOrderExists_ShouldReturnOrder() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertEquals(order, result);
        verify(orderRepository).findById(1L);
    }

    @Test
    void findById_WhenOrderNotFound_ShouldThrowException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> orderService.findById(1L));
        assertEquals("order not found", exception.getMessage());
        verify(orderRepository).findById(1L);
    }

    @Test
    void findAll_WhenOrdersExist_ShouldReturnOrderList() {
        Order order = new Order();
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> result = orderService.findAll();

        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
        verify(orderRepository).findAll();
    }

    @Test
    void findAll_WhenNoOrdersFound_ShouldThrowException() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> orderService.findAll());
        assertEquals("No orders found", exception.getMessage());
        verify(orderRepository).findAll();
    }

    @Test
    void findByUser_WhenUserExists_ShouldReturnOrder() {
        User user = new User();
        Order order = new Order();

        when(orderRepository.findByUser(user)).thenReturn(order);

        Order result = orderService.findByUser(user);

        assertEquals(order, result);
        verify(orderRepository).findByUser(user);
    }

    @Test
    void findByUser_WhenUserIsNull_ShouldThrowException() {
        ApiException exception = assertThrows(ApiException.class, () -> orderService.findByUser(null));
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void findByUser_WhenOrderNotFoundForUser_ShouldThrowException() {
        User user = new User();

        when(orderRepository.findByUser(user)).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> orderService.findByUser(user));
        assertEquals("No order found for user", exception.getMessage());
        verify(orderRepository).findByUser(user);
    }

    @Test
    void update_WhenOrderExists_ShouldUpdateOrder() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);

        Order updatedOrder = new Order();
        updatedOrder.setTotal(200.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order result = orderService.update(1L, updatedOrder);

        assertEquals(200.0, existingOrder.getTotal());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void delete_WhenOrderExists_ShouldDeleteOrder() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.delete(1L);

        assertEquals(order, result);
        verify(orderRepository).findById(1L);
        verify(orderRepository).delete(order);
    }

    @Test
    void save_WhenOrderIsValid_ShouldSaveOrder() {
        Order order = new Order();

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.save(order);

        assertEquals(order, result);
        verify(orderRepository).save(order);
    }

    @Test
    void save_WhenOrderIsNull_ShouldThrowException() {
        ApiException exception = assertThrows(ApiException.class, () -> orderService.save(null));
        assertEquals("Order to update cannot be null", exception.getMessage());
    }
}
