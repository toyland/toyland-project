/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.product.presentaion.dto.ReadProductResponseDto;
import java.util.UUID;
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

  @Override
  public ReadProductResponseDto readProduct(UUID productId) {
    Product product = findProductById(productId);
    return ReadProductResponseDto.from(product);
  }

  private Product findProductById(UUID productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));
  }
}
