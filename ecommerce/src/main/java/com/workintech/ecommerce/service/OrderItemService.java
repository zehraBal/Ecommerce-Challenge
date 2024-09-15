package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.OrderItemResponse;
import com.workintech.ecommerce.entity.Category;

public interface OrderItemService {
    OrderItemResponse findByOrderId(long orderId);
    OrderItemResponse findByProductId(long productId);
}
