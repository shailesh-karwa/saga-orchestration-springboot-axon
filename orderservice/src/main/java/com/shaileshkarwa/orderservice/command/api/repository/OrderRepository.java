package com.shaileshkarwa.orderservice.command.api.repository;

import com.shaileshkarwa.orderservice.command.api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
