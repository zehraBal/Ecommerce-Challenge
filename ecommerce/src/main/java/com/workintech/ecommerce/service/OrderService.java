package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.*;

import java.util.List;

public  interface OrderService {
    Order createOrderFromCart(long cartId, PaymentDetails paymentDetail);
    Order update(long id,Order order);
    Order delete(long id);
    Order save(Order order);
    List<Order> findAll();
    Order findByUser(User user);
    Order findById(long id);

}
