package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.*;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final ShoppingCartService shoppingCartService;
    private OrderRepository orderRepository;
    private final OrderItemService orderItemService;


    @Override
    @Transactional
    public Order createOrderFromCart(long cartId, PaymentDetails paymentDetail) {
        ShoppingCart cart=shoppingCartService.findById(cartId);
        List<CartItem> cartItems=cart.getItems();
        Order newOrder=new Order();
        newOrder.setUser(cart.getUser());
        newOrder.setPaymentDetails(paymentDetail);
        double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        newOrder.setTotal(total);
        newOrder = orderRepository.save(newOrder);
        orderItemService.createOrderItemsFromCart(cartItems,newOrder);
        shoppingCartService.clearCart(cartId);
        return newOrder;
    }

    @Override
    @Transactional
    public Order update(long id, Order order) {
        Order orderToUpdate =findById(id);
        orderToUpdate.setUser(order.getUser());
        orderToUpdate.setTotal(order.getTotal());
        orderToUpdate.setPaymentDetails(order.getPaymentDetails());
        orderToUpdate.setShoppingCart(order.getShoppingCart());
        orderToUpdate.setOrderItems(order.getOrderItems());
        return orderRepository.save(orderToUpdate);
    }

    @Override
    @Transactional
    public Order delete(long id) {
        Order order=findById(id);
        orderRepository.delete(order);
        return order;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id).orElseThrow(()->new ApiException("order not found", HttpStatus.NOT_FOUND));

    }
}
