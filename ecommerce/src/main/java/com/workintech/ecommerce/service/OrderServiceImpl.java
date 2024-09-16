package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.OrderItem;
import com.workintech.ecommerce.entity.User;
import com.workintech.ecommerce.repository.OrderItemRepository;
import com.workintech.ecommerce.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{
    private  OrderItemRepository orderItemRepository;
    private ShoppingCartRepository shoppingCartRepository;

    @Override
    public Order createOrderFromItem(List<OrderItem> orderItems) {
        return null;
    }

    @Override
    public Order update(long id, Order order) {
        return null;
    }

    @Override
    public Order delete(long id) {
        return null;
    }

    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public Order findAll() {
        return null;
    }

    @Override
    public Order findByUser(User user) {
        return null;
    }

    @Override
    public Order findById(long id) {
        return null;
    }
}
