package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.PaymentDetails;
import com.workintech.ecommerce.entity.ShoppingCart;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

        private long shoppingCartId;
        private PaymentDetails paymentDetails;
}
