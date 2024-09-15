package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}
