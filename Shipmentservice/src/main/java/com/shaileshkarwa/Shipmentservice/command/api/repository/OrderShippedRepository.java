package com.shaileshkarwa.Shipmentservice.command.api.repository;

import com.shaileshkarwa.Shipmentservice.command.api.entity.ShippedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderShippedRepository extends JpaRepository<ShippedOrder, String> {
}
