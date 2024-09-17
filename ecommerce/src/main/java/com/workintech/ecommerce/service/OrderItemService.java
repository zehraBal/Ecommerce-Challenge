package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.OrderItem;
import com.workintech.ecommerce.entity.ShoppingCart;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> findAll();
    OrderItem findById(long id);
    List<OrderItem> findByOrderId(long orderId);
    List<OrderItem> findByProductId(long productId);
    OrderItem delete(long id);
    OrderItem update(long id,OrderItem orderItem);
    OrderItem save(OrderItem orderItem);
    List<OrderItem> createOrderItemsFromCart(List<CartItem> cartItems, Order order);
}
