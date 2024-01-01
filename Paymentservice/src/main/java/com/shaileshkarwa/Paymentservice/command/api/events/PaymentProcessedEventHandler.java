package com.shaileshkarwa.Paymentservice.command.api.events;

import com.shaileshkarwa.Commonservice.event.PaymentCancelledEvent;
import com.shaileshkarwa.Commonservice.event.PaymentProcessedEvent;
import com.shaileshkarwa.Paymentservice.command.api.entity.Payment;
import com.shaileshkarwa.Paymentservice.command.api.repository.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentProcessedEventHandler {

    @Autowired
    PaymentRepository paymentRepository;


    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        Payment payment = Payment.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .orderId(paymentProcessedEvent.getOrderId())
                .paymentStatus("Completed")
                .timestamp(new Date())
                .build();
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment
                = paymentRepository.findById(event.getPaymentId()).get();

        payment.setPaymentStatus(event.getPaymentStatus());

        paymentRepository.save(payment);
    }
}
