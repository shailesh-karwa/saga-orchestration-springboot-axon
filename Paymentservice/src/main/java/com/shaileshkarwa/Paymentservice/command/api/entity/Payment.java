package com.shaileshkarwa.Paymentservice.command.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Payment {

    @Id
    private String paymentId;
    private String orderId;
    private Date timestamp;
    String paymentStatus;
}
