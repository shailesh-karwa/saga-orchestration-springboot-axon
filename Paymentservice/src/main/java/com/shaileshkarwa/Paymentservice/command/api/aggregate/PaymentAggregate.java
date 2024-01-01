package com.shaileshkarwa.Paymentservice.command.api.aggregate;

import com.shaileshkarwa.Commonservice.commands.CancelPaymentCommand;
import com.shaileshkarwa.Commonservice.commands.ValidatePaymentCommand;
import com.shaileshkarwa.Commonservice.event.PaymentCancelledEvent;
import com.shaileshkarwa.Commonservice.event.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Slf4j
@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String paymentStatus;

    public PaymentAggregate() {

    }

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        log.info("Executing ValidatePaymentCommand for orderId: {} and paymentId: {}",
                validatePaymentCommand.getOrderId(), validatePaymentCommand.getOrderId());
        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent();
        paymentProcessedEvent.setOrderId(validatePaymentCommand.getOrderId());
        paymentProcessedEvent.setPaymentId(validatePaymentCommand.getPaymentId());
        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
    }

    @CommandHandler
    public void handle(CancelPaymentCommand cancelPaymentCommand) {
        PaymentCancelledEvent paymentCancelledEvent
                = new PaymentCancelledEvent();
        BeanUtils.copyProperties(cancelPaymentCommand,
                paymentCancelledEvent);

        AggregateLifecycle.apply(paymentCancelledEvent);
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent event) {
        this.paymentStatus = event.getPaymentStatus();
    }
}
