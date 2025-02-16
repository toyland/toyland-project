package com.toyland.product.application.usecase;

import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;

public interface ProductService {

  void createProduct(CreateProductServiceRequestDto dto);
}
