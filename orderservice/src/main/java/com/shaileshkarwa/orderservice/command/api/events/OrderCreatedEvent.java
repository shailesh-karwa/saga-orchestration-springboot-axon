package com.shaileshkarwa.orderservice.command.api.events;

import lombok.Data;

@Data
public class OrderCreatedEvent {

    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private int quantity;
    private String orderStatus;
}
