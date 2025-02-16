package com.toyland.product.infrastructure;

import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, UUID> {

}
