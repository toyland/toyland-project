package com.toyland.payment.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.OrderErrorCode;
import com.toyland.order.model.Order;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.payment.model.entity.Payment;
import com.toyland.payment.model.repository.PaymentRepository;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto, Long loginUserId) {

        Order order = orderRepository.findById(requestDto.orderId())
            .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));

        return PaymentResponseDto.from(paymentRepository.save(Payment.of(requestDto, order)));
    }
}
