package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.OrderItemResponse;
import com.workintech.ecommerce.entity.OrderItem;

public class OrderItemConverter {
    public static OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getProduct().getPrice(),
                orderItem.getTotal()

        );
    }
}
