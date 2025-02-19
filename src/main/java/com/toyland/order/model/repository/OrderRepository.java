package com.toyland.order.model.repository;

import com.toyland.order.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order from);

    Optional<Order> findById(UUID orderId);

    // test code 용
    void deleteAllInBatch();

    List<Order> findAll();
}