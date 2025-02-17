/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;

public interface ProductService {

  void createProduct(CreateProductServiceRequestDto dto);
}
