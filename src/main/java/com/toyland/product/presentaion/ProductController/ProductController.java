package com.toyland.product.presentaion.ProductController;

import com.toyland.product.application.facade.ProductFacade;
import com.toyland.product.presentaion.dto.CreateProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductFacade productFacade;

  @PostMapping
  public ResponseEntity<Void> createProduct(@RequestBody CreateProductRequestDto dto) {
    productFacade.createProduct(dto);
    return ResponseEntity.ok().build();
  }

}
