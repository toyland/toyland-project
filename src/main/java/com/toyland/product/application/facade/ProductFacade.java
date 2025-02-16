package com.toyland.product.application.facade;

import com.toyland.product.presentaion.dto.CreateProductRequestDto;

public interface ProductFacade {

  void createProduct(CreateProductRequestDto dto);
}
