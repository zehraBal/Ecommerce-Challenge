package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.ShoppingCart;

public  interface OrderService {
    Order createOrderFromCart(ShoppingCart cart);
}
