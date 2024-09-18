package com.workintech.ecommerce.dto;

import java.sql.Timestamp;

public record PaymentDetailResponse(long id, String username, String paymentType, OrderResponse order, Timestamp createdAt) {
}
