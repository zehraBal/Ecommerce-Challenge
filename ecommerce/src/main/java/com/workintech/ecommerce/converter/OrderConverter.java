package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.OrderResponse;
import com.workintech.ecommerce.dto.OrderItemResponse;
import com.workintech.ecommerce.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderConverter {
    public static OrderResponse convertToOrderResponse(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems().stream()
                .map(com.workintech.ecommerce.converter.OrderItemConverter::convertToOrderItemResponse)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUser().getUsername(),
                orderItems,
                order.getTotal(),
                order.getPaymentDetails().getPaymentType().name()
        );
    }
}
