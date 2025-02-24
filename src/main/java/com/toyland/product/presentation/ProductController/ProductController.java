/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.presentation.ProductController;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.security.annotation.HasManageProductRole;
import com.toyland.product.application.usecase.ProductService;
import com.toyland.product.application.usecase.dto.DeleteProductServiceRequestDto;
import com.toyland.product.application.usecase.dto.UpdateProductServiceRequestDto;
import com.toyland.product.presentation.dto.CreateProductRequestDto;
import com.toyland.product.presentation.dto.ProductResponseDto;
import com.toyland.product.presentation.dto.ProductWithStoreResponseDto;
import com.toyland.product.presentation.dto.SearchProductRequestDto;
import com.toyland.product.presentation.dto.UpdateProductRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
  })
  @HasManageProductRole
  @PostMapping
  public ResponseEntity<Void> createProduct(@RequestBody CreateProductRequestDto dto) {
    ProductResponseDto product = productService.createProduct(dto);
    return ResponseEntity.created(
        UriComponentsBuilder.fromUriString("/api/v1/products/{productId}")
            .buildAndExpand(product.id())
            .toUri()).build();
  }

  @Operation(summary = "상품 단 건 조회", description = "상품을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
  })
  @GetMapping("/{productId}")
  public ResponseEntity<ProductResponseDto> readProduct(@PathVariable UUID productId) {
    return ResponseEntity.ok(productService.readProduct(productId));
  }

  @Operation(summary = "상품 검색", description = "상품을 검색합니다." +
      " (예시 파라미터: ?searchText=가&sort=name,desc,created_at&size=10"
      + "&page=1&storeId=d2a88144-242d-4a87-b607-4846a66d89de&isDisplay=false)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "상품 검색 성공"),
  })
  @GetMapping("/search")
  public ResponseEntity<Page<ProductWithStoreResponseDto>> searchProducts(
      @ModelAttribute SearchProductRequestDto request
  ){
    return ResponseEntity.ok(productService.searchProducts(request));
  }

  @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
  })
  @HasManageProductRole
  @PutMapping("/{productId}")
  public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID productId,
      @RequestBody UpdateProductRequestDto request) {
    return ResponseEntity.ok(productService.updateProduct(
        UpdateProductServiceRequestDto.of(request, productId)));
  }

  @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
  })
  @HasManageProductRole
  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId,
      @CurrentLoginUserId Long currentLoginUserId) {

    productService.deleteProduct(
        DeleteProductServiceRequestDto.of(currentLoginUserId, productId, LocalDateTime.now()));

    return ResponseEntity.noContent().build();
  }
}
