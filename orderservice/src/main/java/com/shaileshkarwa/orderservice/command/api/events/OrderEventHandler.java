package com.shaileshkarwa.orderservice.command.api.events;

import com.shaileshkarwa.Commonservice.event.OrderCancelledEvent;
import com.shaileshkarwa.Commonservice.event.OrderCompletedEvent;
import com.shaileshkarwa.orderservice.command.api.entity.Order;
import com.shaileshkarwa.orderservice.command.api.repository.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderEventHandler {

    @Autowired
    OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        Order order = Order.builder().build();
        BeanUtils.copyProperties(orderCreatedEvent, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent orderCompletedEvent) {
        Optional<Order> orderOptional = orderRepository.findById(orderCompletedEvent.getOrderId());
        Order order = orderOptional.isPresent() ? orderOptional.get() : null;
        order.setOrderStatus(orderCompletedEvent.getOrderStatus());
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCancelledEvent event) {
        Order order
                = orderRepository.findById(event.getOrderId()).get();

        order.setOrderStatus(event.getOrderStatus());

        orderRepository.save(order);
    }
}
