package com.shaileshkarwa.Shipmentservice.command.api.aggregate;

import com.shaileshkarwa.Commonservice.commands.OrderShippedCommand;
import com.shaileshkarwa.Commonservice.event.OrderShippedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class OrderShippedAggregate {

    @AggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String shippingStatus;

    public OrderShippedAggregate() {

    }

    @CommandHandler
    public OrderShippedAggregate(OrderShippedCommand orderShippedCommand) {
        OrderShippedEvent orderShippedEvent = OrderShippedEvent.builder()
                .orderId(orderShippedCommand.getOrderId())
                .shipmentId(orderShippedCommand.getShipmentId())
                .shippingStatus("COMPLETED")
                .build();
        AggregateLifecycle.apply(orderShippedEvent);
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        this.shipmentId = orderShippedEvent.getShipmentId();
        this.orderId = orderShippedEvent.getOrderId();
        this.shippingStatus = orderShippedEvent.getShippingStatus();
    }

}
