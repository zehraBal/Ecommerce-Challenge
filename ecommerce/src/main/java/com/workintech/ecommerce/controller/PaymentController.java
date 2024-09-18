package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.converter.PaymentDetailConverter;
import com.workintech.ecommerce.dto.PaymentDetailResponse;
import com.workintech.ecommerce.entity.PaymentDetails;
import com.workintech.ecommerce.service.PaymentDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/payment")
@Validated
public class PaymentController {

    private PaymentDetailsService pdService;

    @GetMapping
    public List<PaymentDetailResponse>  getAll(){
        return PaymentDetailConverter.convertToPaymentDetailResponseList(pdService.findAll());
    }

    @GetMapping("/{id}")
    public PaymentDetailResponse getById(@PathVariable long id){
        return PaymentDetailConverter.convertToPaymentDetailResponse(pdService.findById(id));
    }

    @GetMapping("/byOrder/{id}")
    public PaymentDetailResponse getByOrderId(@PathVariable long orderId){
        return PaymentDetailConverter.convertToPaymentDetailResponse(pdService.findByOrderId(orderId));
    }

    @PostMapping
    public PaymentDetailResponse save(@RequestBody PaymentDetails paymentDetail){
        return PaymentDetailConverter.convertToPaymentDetailResponse(pdService.save(paymentDetail));
    }

    @PutMapping("/{id}")
    public PaymentDetailResponse update(@PathVariable long id,@RequestBody PaymentDetails paymentDetail){
        return PaymentDetailConverter.convertToPaymentDetailResponse(pdService.update(id,paymentDetail));
    }

    @DeleteMapping("/{id}")
    public PaymentDetailResponse delete(@PathVariable long id){
        return PaymentDetailConverter.convertToPaymentDetailResponse(pdService.delete(id));
    }
}
