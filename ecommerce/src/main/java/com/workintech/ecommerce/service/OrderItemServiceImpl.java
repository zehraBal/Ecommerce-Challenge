package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.OrderItem;
import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.OrderItemRepository;
import com.workintech.ecommerce.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{
    private OrderItemRepository oiRepository;
    private ShoppingCartRepository scRepository;

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
    public List<OrderItem> createOrderItemsFromCart(ShoppingCart shoppingCart) {
        Order newOrder = new Order();
        newOrder.setUser(shoppingCart.getUser());
      //  newOrder.setTotalPrice(calculateTotalPrice(shoppingCart));
      //  newOrder.setOrderStatus("CREATED");

        // Alışveriş sepetindeki ürünleri sipariş öğelerine dönüştür
        List<OrderItem> orderItems = shoppingCart.getProducts().stream()
                .map(product -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setOrder(newOrder);  // Her OrderItem için Order ilişkisini kuruyoruz
                    orderItem.setQuantity(1);  // Örnek olarak quantity=1
                    orderItem.setPrice(product.getPrice());
                    return orderItem;
                })
                .toList();

        oiRepository.saveAll(orderItems);

        return orderItems;
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
}
