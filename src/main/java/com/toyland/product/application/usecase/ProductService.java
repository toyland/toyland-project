/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import com.toyland.product.application.usecase.dto.DeleteProductServiceRequestDto;
import com.toyland.product.application.usecase.dto.UpdateProductServiceRequestDto;
import com.toyland.product.presentation.dto.CreateProductRequestDto;
import com.toyland.product.presentation.dto.ProductResponseDto;
import com.toyland.product.presentation.dto.ProductWithStoreResponseDto;
import com.toyland.product.presentation.dto.SearchProductRequestDto;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ProductService {

  ProductResponseDto createProduct(CreateProductRequestDto dto);

  ProductResponseDto readProduct(UUID productId);

  Page<ProductWithStoreResponseDto> searchProducts(SearchProductRequestDto request);

  ProductResponseDto updateProduct(UpdateProductServiceRequestDto of);

  void deleteProduct(DeleteProductServiceRequestDto dto);
}
