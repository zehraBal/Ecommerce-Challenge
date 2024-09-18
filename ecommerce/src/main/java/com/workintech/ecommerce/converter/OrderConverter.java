package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.OrderResponse;
import com.workintech.ecommerce.dto.OrderItemResponse;
import com.workintech.ecommerce.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderConverter {
    public static OrderResponse convertToOrderResponse(Order order) {
        List<OrderItemResponse> items= OrderItemConverter.convertToOrderItemResponseList(order.getOrderItems());
        return new OrderResponse(order.getId(), order.getUser().getUsername(),items,order.getTotal(),order.getPaymentDetails().getPaymentType().toString());
    }

    public static List<OrderResponse> convertToOrderResponseList(List<Order> orders) {
        List<OrderResponse> orderResponses=new ArrayList<>();
        for(Order o:orders){
            orderResponses.add(convertToOrderResponse(o));
        }
        return orderResponses;
    }

    }
