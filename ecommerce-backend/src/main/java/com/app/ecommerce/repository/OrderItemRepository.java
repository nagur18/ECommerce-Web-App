package com.app.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.ecommerce.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}