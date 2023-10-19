package com.accio.robotwarehouse.repository;

import com.accio.robotwarehouse.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
