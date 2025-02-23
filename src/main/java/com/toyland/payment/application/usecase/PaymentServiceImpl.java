package com.toyland.payment.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.OrderErrorCode;
import com.toyland.global.exception.type.domain.PaymentErrorCode;
import com.toyland.order.model.Order;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.payment.model.entity.Payment;
import com.toyland.payment.model.repository.PaymentRepository;
import com.toyland.payment.presentation.dto.request.PaymentRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentSearchRequestDto;
import com.toyland.payment.presentation.dto.request.PaymentUpdateRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentSearchResponseDto;
import com.toyland.payment.presentation.dto.response.PaymentUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

        return PaymentResponseDto.from(paymentRepository.save(Payment.of(order)));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto findByPaymentId(UUID paymentId) {
        return PaymentResponseDto.from(paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        CustomException.from(PaymentErrorCode.PAYMENT_NOT_FOUND)));
    }

    @Override
    public PaymentUpdateResponseDto updatePayment(PaymentUpdateRequestDto requestDto) {

        Payment payment = paymentRepository.findById(requestDto.id())
                .orElseThrow(() -> CustomException.from(PaymentErrorCode.PAYMENT_NOT_FOUND));

        Order order = orderRepository.findById(requestDto.orderId())
                .orElseThrow(() -> CustomException.from(OrderErrorCode.ORDER_NOT_FOUND));


        payment.addPayment(requestDto, order);

        return PaymentUpdateResponseDto.from(requestDto);
    }

    @Override
    public void deletePayment(UUID paymentId, Long loginUserId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> CustomException.from(PaymentErrorCode.PAYMENT_NOT_FOUND));

        payment.addDeletedField(loginUserId);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentSearchResponseDto> searchPayment(PaymentSearchRequestDto searchRequestDto, Pageable pageable) {
        return paymentRepository.searchPayment(searchRequestDto, pageable);
    }


}
