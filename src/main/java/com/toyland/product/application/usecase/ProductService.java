/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;
import com.toyland.product.presentaion.dto.ReadProductResponseDto;
import java.util.UUID;

public interface ProductService {

  void createProduct(CreateProductServiceRequestDto dto);

  ReadProductResponseDto readProduct(UUID productId);
}
