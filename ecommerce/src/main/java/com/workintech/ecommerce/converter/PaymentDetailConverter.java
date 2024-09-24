package com.workintech.ecommerce.converter;

import com.workintech.ecommerce.dto.OrderResponse;
import com.workintech.ecommerce.dto.PaymentDetailResponse;
import com.workintech.ecommerce.entity.PaymentDetails;

import java.util.ArrayList;
import java.util.List;

public class PaymentDetailConverter {
    public static PaymentDetailResponse convertToPaymentDetailResponse(PaymentDetails detail){
        return new PaymentDetailResponse(detail.getId(),detail.getUser().getUsername(),detail.getPaymentType().toString(),detail.getOrder().getId(),detail.getCreatedAt());
    }

    public static List<PaymentDetailResponse> convertToPaymentDetailResponseList(List<PaymentDetails> details){
        List<PaymentDetailResponse> detailResponses=new ArrayList<>();
        for(PaymentDetails d:details){
            detailResponses.add(convertToPaymentDetailResponse(d));
        }
        return detailResponses;
    }
}
