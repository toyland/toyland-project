/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.presentaion.ProductController;

import com.toyland.product.application.facade.ProductFacade;
import com.toyland.product.application.usecase.ProductService;
import com.toyland.product.presentaion.dto.CreateProductRequestDto;
import com.toyland.product.presentaion.dto.ReadProductResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductFacade productFacade;
  private final ProductService productService;

  /**
   * 상품을 생성합니다.
   * @param dto 상품 생성 정보
   * @return 200 ok
   */
  @PostMapping
  public ResponseEntity<Void> createProduct(@RequestBody CreateProductRequestDto dto) {
    productFacade.createProduct(dto);
    return ResponseEntity.ok().build();
  }

  /**
   * 상품을 조회합니다.
   * @param productId 조회할 상품 id
   * @return 조회 상품 dto
   */
  @GetMapping("/{productId}")
  public ResponseEntity<ReadProductResponseDto> readProduct(@PathVariable UUID productId) {
    return ResponseEntity.ok(productService.readProduct(productId));
  }
}
