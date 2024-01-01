package com.shaileshkarwa.Shipmentservice.command.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShippedOrder {
    @Id
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
