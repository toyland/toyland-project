package com.toyland.order.model.repository;

import com.toyland.order.model.Order;
import com.toyland.order.presentation.dto.request.OrderSearchRequestDto;
import com.toyland.order.presentation.dto.response.OrderSearchResponseDto;
import com.toyland.user.model.UserRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order from);

    Optional<Order> findById(UUID orderId);

    Page<OrderSearchResponseDto> searchOrder(OrderSearchRequestDto searchRequestDto,
                                             Pageable pageable, Long loginUserId, UserRoleEnum role);

    // test code 용
    void deleteAllInBatch();

  List<Order> findAll();
}