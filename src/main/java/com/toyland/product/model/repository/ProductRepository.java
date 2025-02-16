package com.toyland.product.model.repository;

import com.toyland.product.model.entity.Product;
import java.util.List;

public interface ProductRepository {
  Product save(Product product);


  // test code 용
  void deleteAllInBatch();

  List<Product> findAll();
}
