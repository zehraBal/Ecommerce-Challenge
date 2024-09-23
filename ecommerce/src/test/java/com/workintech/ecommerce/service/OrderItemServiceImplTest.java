package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.OrderItem;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.OrderItemRepository;
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
class OrderItemServiceImplTest {

    private OrderItemService orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        orderItemService = new OrderItemServiceImpl(orderItemRepository, null); // CartItemRepository kullanılmıyor
    }

    @Test
    void findAll_ShouldReturnOrderItems() {
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.findAll()).thenReturn(List.of(orderItem));

        List<OrderItem> result = orderItemService.findAll();

        assertEquals(1, result.size());
        assertEquals(orderItem, result.get(0));
        verify(orderItemRepository).findAll();
    }

    @Test
    void findById_WhenOrderItemExists_ShouldReturnOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        OrderItem result = orderItemService.findById(1L);

        assertEquals(orderItem, result);
        verify(orderItemRepository).findById(1L);
    }

    @Test
    void findById_WhenOrderItemNotFound_ShouldThrowException() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> orderItemService.findById(1L));
        assertEquals("Order item not found", exception.getMessage());
        verify(orderItemRepository).findById(1L);
    }

    @Test
    void findByOrderId_WhenOrderItemsExist_ShouldReturnList() {
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.findByOrderId(1L)).thenReturn(List.of(orderItem));

        List<OrderItem> result = orderItemService.findByOrderId(1L);

        assertEquals(1, result.size());
        assertEquals(orderItem, result.get(0));
        verify(orderItemRepository).findByOrderId(1L);
    }

    @Test
    void findByOrderId_WhenNoOrderItemsFound_ShouldThrowException() {
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> orderItemService.findByOrderId(1L));
        assertEquals("No order items found for the given order ID", exception.getMessage());
        verify(orderItemRepository).findByOrderId(1L);
    }

    @Test
    void findByProductId_WhenOrderItemsExist_ShouldReturnList() {
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.findByProductId(1L)).thenReturn(List.of(orderItem));

        List<OrderItem> result = orderItemService.findByProductId(1L);

        assertEquals(1, result.size());
        assertEquals(orderItem, result.get(0));
        verify(orderItemRepository).findByProductId(1L);
    }

    @Test
    void findByProductId_WhenNoOrderItemsFound_ShouldThrowException() {
        when(orderItemRepository.findByProductId(1L)).thenReturn(Collections.emptyList());

        ApiException exception = assertThrows(ApiException.class, () -> orderItemService.findByProductId(1L));
        assertEquals("No order items found for the given product ID", exception.getMessage());
        verify(orderItemRepository).findByProductId(1L);
    }

    @Test
    void delete_WhenOrderItemExists_ShouldDeleteOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        OrderItem result = orderItemService.delete(1L);

        assertEquals(orderItem, result);
        verify(orderItemRepository).findById(1L);
        verify(orderItemRepository).delete(orderItem);
    }

    @Test
    void delete_WhenOrderItemNotFound_ShouldThrowException() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> orderItemService.delete(1L));
        assertEquals("Order item not found for deletion", exception.getMessage());
        verify(orderItemRepository).findById(1L);
    }

    @Test
    void update_WhenOrderItemExists_ShouldUpdateOrderItem() {
        OrderItem existingOrderItem = new OrderItem();
        existingOrderItem.setId(1L);

        OrderItem updatedOrderItem = new OrderItem();
        updatedOrderItem.setPrice(200.0);

        when(orderItemRepository.existsById(1L)).thenReturn(true);
        when(orderItemRepository.save(updatedOrderItem)).thenReturn(updatedOrderItem);

        OrderItem result = orderItemService.update(1L, updatedOrderItem);

        assertEquals(updatedOrderItem.getPrice(), result.getPrice());
        verify(orderItemRepository).existsById(1L);
        verify(orderItemRepository).save(updatedOrderItem);
    }

    @Test
    void update_WhenOrderItemNotFound_ShouldThrowException() {
        when(orderItemRepository.existsById(1L)).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> orderItemService.update(1L, new OrderItem()));
        assertEquals("Order item not found for update", exception.getMessage());
        verify(orderItemRepository).existsById(1L);
    }

    @Test
    void save_WhenOrderItemIsNew_ShouldSaveOrderItem() {
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        OrderItem result = orderItemService.save(orderItem);

        assertEquals(orderItem, result);
        verify(orderItemRepository).save(orderItem);
    }

    @Test
    void save_WhenOrderItemAlreadyExists_ShouldThrowException() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        when(orderItemRepository.existsById(1L)).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> orderItemService.save(orderItem));
        assertEquals("Order item already exists", exception.getMessage());
        verify(orderItemRepository).existsById(1L);
    }

    @Test
    void createOrderItemsFromCart_ShouldCreateOrderItemsFromCartItems() {
        CartItem cartItem = new CartItem();
        cartItem.setPrice(100.0);
        cartItem.setQuantity(2);

        Order order = new Order();
        order.setId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setPrice(cartItem.getPrice());
        orderItem.setQuantity(cartItem.getQuantity());

        when(orderItemRepository.saveAll(anyList())).thenReturn(List.of(orderItem));

        List<OrderItem> result = orderItemService.createOrderItemsFromCart(List.of(cartItem), order);

        assertEquals(1, result.size());
        assertEquals(orderItem, result.get(0));
        verify(orderItemRepository).saveAll(anyList());
    }
}
