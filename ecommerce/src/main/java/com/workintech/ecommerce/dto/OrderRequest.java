package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.PaymentDetails;
import com.workintech.ecommerce.entity.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class OrderRequest {

        private long shoppingCartId;
        private PaymentDetails paymentDetails;
}
