package com.toyland.payment.model.repository;

import com.toyland.payment.model.entity.Payment;
import com.toyland.payment.presentation.dto.request.PaymentSearchRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);

    Page<PaymentSearchResponseDto> searchPayment(PaymentSearchRequestDto searchRequestDto, Pageable pageable);

}
