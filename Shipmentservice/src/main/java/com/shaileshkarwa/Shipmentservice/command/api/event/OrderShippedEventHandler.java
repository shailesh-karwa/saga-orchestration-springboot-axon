package com.shaileshkarwa.Shipmentservice.command.api.event;

import com.shaileshkarwa.Commonservice.event.OrderShippedEvent;
import com.shaileshkarwa.Shipmentservice.command.api.entity.ShippedOrder;
import com.shaileshkarwa.Shipmentservice.command.api.repository.OrderShippedRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderShippedEventHandler {

    @Autowired
    OrderShippedRepository orderShippedRepository;

    @EventHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        ShippedOrder shippedOrder = ShippedOrder.builder()
                .shipmentId(orderShippedEvent.getShipmentId())
                .orderId(orderShippedEvent.getOrderId())
                .shipmentStatus(orderShippedEvent.getShippingStatus())
                .build();
        orderShippedRepository.save(shippedOrder);
    }
}
