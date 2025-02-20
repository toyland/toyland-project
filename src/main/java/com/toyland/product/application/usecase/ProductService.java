/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import com.toyland.product.application.usecase.dto.DeleteProductServiceRequestDto;
import com.toyland.product.application.usecase.dto.UpdateProductServiceRequestDto;
import com.toyland.product.presentation.dto.CreateProductRequestDto;
import com.toyland.product.presentation.dto.ProductResponseDto;
import java.util.UUID;

public interface ProductService {

  void createProduct(CreateProductRequestDto dto);

  ProductResponseDto readProduct(UUID productId);

  ProductResponseDto updateProduct(UpdateProductServiceRequestDto of);

  void deleteProduct(DeleteProductServiceRequestDto dto);
}
