package com.toyland.order.model.repository;

import com.toyland.order.model.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order from);

    // test code 용
    void deleteAllInBatch();

    List<Order> findAll();
}