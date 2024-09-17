package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByUser(User user);
}
