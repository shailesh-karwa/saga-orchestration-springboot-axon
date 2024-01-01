package com.shaileshkarwa.orderservice.command.api.saga;

import com.shaileshkarwa.Commonservice.commands.*;
import com.shaileshkarwa.Commonservice.event.*;
import com.shaileshkarwa.Commonservice.model.User;
import com.shaileshkarwa.Commonservice.queries.GetUserPaymentDetailsQuery;
import com.shaileshkarwa.orderservice.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        log.info("OrderCreateEven in Saga for orderId : {}", orderCreatedEvent.getOrderId());
        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(orderCreatedEvent.getUserId());
        User user = null;
        try {
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            //Compensating transaction to cancel the order
            cancelOrderCommand(orderCreatedEvent.getOrderId());
        }
        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .paymentId(UUID.randomUUID().toString())
                .orderId(orderCreatedEvent.getOrderId())
                .cardDetails(user.getCardDetails())
                .build();
        commandGateway.sendAndWait(validatePaymentCommand);
    }

    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand cancelOrderCommand
                = new CancelOrderCommand(orderId);
        commandGateway.send(cancelOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("PaymentProcessedEvent in Saga for orderId: {} and paymentId: {}",
                paymentProcessedEvent.getOrderId(), paymentProcessedEvent.getPaymentId());
        try {
            OrderShippedCommand orderShippedCommand = OrderShippedCommand.builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(paymentProcessedEvent.getOrderId())
                    .build();

            commandGateway.sendAndWait(orderShippedCommand);
        } catch (Exception exception) {
            //Compensating transaction
            log.error(exception.getMessage());
            cancelPaymentCommand(paymentProcessedEvent);
        }
    }

    private void cancelPaymentCommand(PaymentProcessedEvent event) {
        CancelPaymentCommand cancelPaymentCommand
                = new CancelPaymentCommand(
                event.getPaymentId(), event.getOrderId()
        );

        commandGateway.send(cancelPaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(OrderShippedEvent orderShippedEvent) {
        log.info("OrderShippedEvent in Saga for orderId : {} ",
                orderShippedEvent.getOrderId());
        OrderCompleteCommand orderCompleteCommand = OrderCompleteCommand.builder()
                .orderId(orderShippedEvent.getOrderId())
                .orderStatus("COMPLETED")
                .build();
        commandGateway.sendAndWait(orderCompleteCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handler(OrderCompletedEvent orderCompletedEvent) {
        log.info("OrderCompletedEvent in Saga for orderId : {} ",
                orderCompletedEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCancelledEvent event) {
        log.info("OrderCancelledEvent in Saga for Order Id : {}",
                event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent event) {
        log.info("PaymentCancelledEvent in Saga for Order Id : {}",
                event.getOrderId());
        cancelOrderCommand(event.getOrderId());
    }

}
