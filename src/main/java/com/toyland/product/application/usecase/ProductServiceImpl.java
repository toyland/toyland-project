/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import com.toyland.global.exception.type.domain.StoreErrorCode;
import com.toyland.product.application.usecase.dto.DeleteProductServiceRequestDto;
import com.toyland.product.application.usecase.dto.UpdateProductServiceRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.product.presentation.dto.CreateProductRequestDto;
import com.toyland.product.presentation.dto.ProductResponseDto;
import com.toyland.product.presentation.dto.ProductWithStoreResponseDto;
import com.toyland.product.presentation.dto.SearchProductRequestDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;

  @Override
  public void createProduct(CreateProductRequestDto dto) {
    Store store = findStoreById(dto.storeId());
    productRepository.save(Product.of(dto, store));
  }

  @Override
  public ProductResponseDto readProduct(UUID productId) {
    Product product = findProductById(productId);
    return ProductResponseDto.from(product);
  }

  @Override
  public Page<ProductWithStoreResponseDto> searchProducts(SearchProductRequestDto dto) {
    return productRepository.searchProducts(dto);
  }

  @Override
  @Transactional
  public ProductResponseDto updateProduct(UpdateProductServiceRequestDto dto) {
    Product product = findProductById(dto.id());
    product.update(dto);
    return ProductResponseDto.from(product);
  }

  @Override
  @Transactional
  public void deleteProduct(DeleteProductServiceRequestDto dto) {
    Product product = findProductById(dto.productId());
    product.delete(dto.eventDateTime(), dto.actorId());
  }

  private Store findStoreById(UUID storeId) {
    return storeRepository.findById(storeId).orElseThrow(
        () -> CustomException.from(StoreErrorCode.STORE_NOT_FOUND)
    );
  }

  private Product findProductById(UUID productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));
  }
}
