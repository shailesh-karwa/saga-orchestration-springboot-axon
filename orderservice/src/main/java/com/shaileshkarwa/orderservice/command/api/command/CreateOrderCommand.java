package com.shaileshkarwa.orderservice.command.api.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.AggregateIdentifier;

@Data
@Builder
public class CreateOrderCommand {
    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private int quantity;
    private String orderStatus;
}
