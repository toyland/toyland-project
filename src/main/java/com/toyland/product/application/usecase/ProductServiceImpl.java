/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  public void createProduct(CreateProductServiceRequestDto dto) {
    productRepository.save(Product.from(dto));
  }
}
