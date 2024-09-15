package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {
}
