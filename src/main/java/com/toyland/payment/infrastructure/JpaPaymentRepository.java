package com.toyland.payment.infrastructure;

import com.toyland.payment.model.entity.Payment;
import com.toyland.payment.model.repository.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
public interface JpaPaymentRepository extends JpaRepository<Payment, UUID>, PaymentRepository, JpaPaymentRepositoryCustom {

}
