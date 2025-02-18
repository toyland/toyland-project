/**
 * @Date : 2025. 02. 17.
 * @author : jinyoung(jin2304)
 */
package com.toyland.orderproduct.infrastructure;

import com.toyland.orderproduct.model.OrderProduct;
import com.toyland.orderproduct.model.repository.OrderProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderProductRepository extends OrderProductRepository, JpaRepository<OrderProduct, UUID> {

}
