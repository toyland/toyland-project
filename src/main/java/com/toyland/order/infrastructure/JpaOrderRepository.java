/**
 * @Date : 2025. 02. 17.
 * @author : jinyoung(jin2304)
 */
package com.toyland.order.infrastructure;

import com.toyland.order.model.Order;
import com.toyland.order.model.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface JpaOrderRepository extends OrderRepository, JpaRepository<Order, UUID>,
        JpaOrderRepositoryCustom {

}
