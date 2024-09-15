package com.workintech.ecommerce.dto;

import java.util.List;

public record OrderResponse(Long orderId,String userName,List<OrderItemResponse> orderItems,double totalPrice,String paymentType) {
}