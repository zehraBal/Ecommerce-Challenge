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
        if (cart == null || cart.getItems().isEmpty()) {
            throw new ApiException("Shopping cart is empty or does not exist", HttpStatus.BAD_REQUEST);
        }
        if (paymentDetail == null) {
            throw new ApiException("Payment details cannot be null", HttpStatus.BAD_REQUEST);
        }
        List<CartItem> cartItems=cart.getItems();
        Order newOrder=new Order();
        newOrder.setUser(cart.getUser());
        newOrder.setPaymentDetails(paymentDetail);
        double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        newOrder.setTotal(total);
        if (total <= 0) {
            throw new ApiException("Total order amount cannot be zero or negative", HttpStatus.BAD_REQUEST);
        }
        newOrder = orderRepository.save(newOrder);
        orderItemService.createOrderItemsFromCart(cartItems,newOrder);
        shoppingCartService.clearCart(cartId);
        return newOrder;
    }

    @Override
    @Transactional
    public Order update(long id, Order order) {
        Order orderToUpdate =findById(id);
        if (order == null) {
            throw new ApiException("Order to update cannot be null", HttpStatus.BAD_REQUEST);
        }
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
        if (order == null) {
            throw new ApiException("Order to update cannot be null", HttpStatus.BAD_REQUEST);
        }
        orderRepository.delete(order);
        return order;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        if (order == null) {
            throw new ApiException("Order to update cannot be null", HttpStatus.BAD_REQUEST);
        }
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new ApiException("No orders found", HttpStatus.NOT_FOUND);
        }
        return orders;    }

    @Override
    public Order findByUser(User user) {
        if (user == null) {
            throw new ApiException("User cannot be null", HttpStatus.BAD_REQUEST);
        }

        Order order = orderRepository.findByUser(user);
        if (order == null) {
            throw new ApiException("No order found for user", HttpStatus.NOT_FOUND);
        }

        return order;    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id).orElseThrow(()->new ApiException("order not found", HttpStatus.NOT_FOUND));

    }
}
