package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.OrderItem;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.entity.User;

import java.util.List;

public  interface OrderService {
    Order createOrderFromItem(List<OrderItem> orderItems);
    Order update(long id,Order order);
    Order delete(long id);
    Order save(Order order);
    Order findAll();
    Order findByUser(User user);
    Order findById(long id);
}
