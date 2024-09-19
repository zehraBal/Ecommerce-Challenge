package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.PaymentDetails;
import com.workintech.ecommerce.exception.ApiException;
import com.workintech.ecommerce.repository.OrderRepository;
import com.workintech.ecommerce.repository.PaymentDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    private PaymentDetailsRepository pdRepository;
    private OrderRepository orderRepository;

    @Override
    public List<PaymentDetails> findAll() {
        List<PaymentDetails> details=pdRepository.findAll();
        if (details.isEmpty()) {
            throw new ApiException("No payment detail found", HttpStatus.NOT_FOUND);
        }
        return  details;
    }

    @Override
    public PaymentDetails findById(long id) {
        return pdRepository.findById(id).orElseThrow(()->new ApiException("Payment detail not found.", HttpStatus.NOT_FOUND));
    }

    @Override
    public PaymentDetails findByOrderId(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new ApiException("Order not found.",HttpStatus.NOT_FOUND));
       PaymentDetails detail = pdRepository.findByOrderId(order.getId());
       if(detail==null){
           throw new ApiException("Payment detail not found for order ID " + orderId, HttpStatus.NOT_FOUND);
       }
       return detail;
    }

    @Override
    public PaymentDetails delete(long id) {
        PaymentDetails details= findById(id);
        pdRepository.delete(details);
        return details;
    }

    @Override
    public PaymentDetails save(PaymentDetails paymentDetails) {
        if (paymentDetails == null) {
            throw new ApiException("Payment detail cannot be null", HttpStatus.BAD_REQUEST);
        }
        if(pdRepository.findByOrderId(paymentDetails.getOrder().getId())!=null){
            throw new ApiException("Payment detail already exists", HttpStatus.CONFLICT);
        }
        return pdRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails update(long id, PaymentDetails paymentDetails) {
        PaymentDetails details=findById(id);
        details.setUser(paymentDetails.getUser());
        details.setPaymentType(paymentDetails.getPaymentType());
        details.setOrder(paymentDetails.getOrder());
        return pdRepository.save(details);
    }
}
