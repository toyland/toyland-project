package com.toyland.payment.model.repository;

import com.toyland.payment.model.entity.Payment;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
public interface PaymentRepository {

    Payment save(Payment payment);

}
