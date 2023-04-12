package com.fmss.paymentservice.repository;

import com.fmss.paymentservice.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findPaymentByOrderId(String orderId);
}
