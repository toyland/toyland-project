package com.toyland.payment.model.repository;

import com.toyland.payment.model.entity.Payment;

import java.util.Optional;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
public interface PaymentRepository {

    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);

}
