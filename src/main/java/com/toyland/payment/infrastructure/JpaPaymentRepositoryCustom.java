package com.toyland.payment.infrastructure;

import com.toyland.payment.presentation.dto.request.PaymentSearchRequestDto;
import com.toyland.payment.presentation.dto.response.PaymentSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JpaPaymentRepositoryCustom {

    Page<PaymentSearchResponseDto> searchPayment(PaymentSearchRequestDto searchRequestDto,
                                                 Pageable pageable);
}
