package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.ShoppingCart;
import com.workintech.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    @Query("SELECT s FROM ShoppingCart s WHERE s.user = :user")
    ShoppingCart findByUser(User user);

}
