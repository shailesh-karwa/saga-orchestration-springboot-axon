package com.shaileshkarwa.Paymentservice.command.api.repository;

import com.shaileshkarwa.Paymentservice.command.api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
