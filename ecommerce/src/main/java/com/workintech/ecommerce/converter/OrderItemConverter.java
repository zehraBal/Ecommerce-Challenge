package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.OrderItemResponse;
import com.workintech.ecommerce.entity.OrderItem;

import java.util.ArrayList;
import java.util.List;

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

    public static List<OrderItemResponse>  convertToOrderItemResponseList(List<OrderItem> orderItems){
        List<OrderItemResponse> responses = new ArrayList<>();
        for(OrderItem o : orderItems){
            responses.add(convertToOrderItemResponse(o));
        }
        return responses;
    }
}
