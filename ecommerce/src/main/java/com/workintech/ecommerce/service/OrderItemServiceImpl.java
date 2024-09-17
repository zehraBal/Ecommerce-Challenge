package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.CartItem;
import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.OrderItem;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.CartItemRepository;
import com.workintech.ecommerce.repository.OrderItemRepository;
import com.workintech.ecommerce.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{

    private OrderItemRepository oiRepository;
    private CartItemRepository cartItemRepository;

    @Override
    public List<OrderItem> findAll() {
        return oiRepository.findAll();
    }

    @Override
    public OrderItem findById(long id) {
        return oiRepository.findById(id).orElseThrow(()->new ApiException("Order item not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<OrderItem> findByOrderId(long orderId) {
        return oiRepository.findByOrderId(orderId);
    }

    @Override
    public List<OrderItem> findByProductId(long productId) {
        return oiRepository.findByProductId(productId);
    }

    @Override
    public OrderItem delete(long id) {
        return null;
    }

    @Override
    public OrderItem update(long id, OrderItem orderItem) {
        return null;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return null;
    }

    @Override
    public List<OrderItem> createOrderItemsFromCart(List<CartItem> cartItems, Order order) {
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setTotal(cartItem.getPrice() * cartItem.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        return oiRepository.saveAll(orderItems);

    }
}
