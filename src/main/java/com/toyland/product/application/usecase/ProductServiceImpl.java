/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;
import com.toyland.product.application.usecase.dto.UpdateProductServiceRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.product.presentaion.dto.ProductResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  public void createProduct(CreateProductServiceRequestDto dto) {
    productRepository.save(Product.from(dto));
  }

  @Override
  public ProductResponseDto readProduct(UUID productId) {
    Product product = findProductById(productId);
    return ProductResponseDto.from(product);
  }

  @Override
  @Transactional
  public ProductResponseDto updateProduct(UpdateProductServiceRequestDto dto) {
    Product product = findProductById(dto.id());
    product.update(dto);
    return ProductResponseDto.from(product);
  }

  private Product findProductById(UUID productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));
  }
}
