/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.model.repository;

import com.toyland.product.model.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
  Product save(Product product);

  Optional<Product> findById(UUID productId);

  // test code 용
  <S extends Product> Iterable<S> saveAll(Iterable<S> entities);

  void deleteAllInBatch();

  List<Product> findAll();

  Optional<Product> findById(UUID id);
}
