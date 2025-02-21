/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.model.repository;

import com.toyland.product.model.entity.Product;
import com.toyland.product.presentation.dto.ProductWithStoreResponseDto;
import com.toyland.product.presentation.dto.SearchProductRequestDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ProductRepository {

  Product save(Product product);

  Optional<Product> findById(UUID productId);

  Page<ProductWithStoreResponseDto> searchProducts(SearchProductRequestDto dto);

  // test code 용

  <S extends Product> Iterable<S> saveAll(Iterable<S> entities);

  void deleteAllInBatch();

  List<Product> findAll();

}
