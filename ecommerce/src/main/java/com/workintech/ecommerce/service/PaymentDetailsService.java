package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.PaymentDetails;

import java.util.List;

public interface PaymentDetailsService {
    List<PaymentDetails> findAll();
    PaymentDetails findById(long id);
    PaymentDetails findByOrderId(long orderId);
    PaymentDetails delete(long id);
    PaymentDetails save(PaymentDetails paymentDetails);
    PaymentDetails update(long id,PaymentDetails paymentDetails);

}
