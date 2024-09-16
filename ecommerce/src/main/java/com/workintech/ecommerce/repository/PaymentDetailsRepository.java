package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {

    @Query("SELECT pd FROM PaymentDetails pd WHERE pd.order.id = :orderId")
    PaymentDetails findByOrderId(long orderId);
}
