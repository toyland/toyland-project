package com.toyland.order.infrastructure;

import com.toyland.order.presentation.dto.request.OrderSearchRequestDto;
import com.toyland.order.presentation.dto.response.OrderSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JpaOrderRepositoryCustom {

    Page<OrderSearchResponseDto> searchOrder(OrderSearchRequestDto searchRequestDto,
                                              Pageable pageable);
}
