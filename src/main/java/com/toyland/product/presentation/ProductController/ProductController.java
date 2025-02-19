/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.presentation.ProductController;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.product.application.facade.ProductFacade;
import com.toyland.product.application.usecase.ProductService;
import com.toyland.product.application.usecase.dto.DeleteProductServiceRequestDto;
import com.toyland.product.application.usecase.dto.UpdateProductServiceRequestDto;
import com.toyland.product.presentation.dto.CreateProductRequestDto;
import com.toyland.product.presentation.dto.ProductResponseDto;
import com.toyland.product.presentation.dto.UpdateProductRequestDto;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
     *
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
     *
     * @param productId 조회할 상품 id
     * @return 조회 상품 dto
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> readProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.readProduct(productId));
    }

    /**
     * 상품을 수정합니다.
     *
     * @param productId 수정할 product id
     * @param request   수정 내용
     * @return 수정된 내용
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID productId,
        @RequestBody UpdateProductRequestDto request) {
        return ResponseEntity.ok(productService.updateProduct(
            UpdateProductServiceRequestDto.of(request, productId)));
    }

    /**
     * 상품을 삭제합니다.
     *
     * @param productId 삭제할 상품 id
     * @return 204 no content
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId,
        @CurrentLoginUserId Long currentLoginUserId) {

        productService.deleteProduct(
            DeleteProductServiceRequestDto.of(currentLoginUserId, productId, LocalDateTime.now()));

        return ResponseEntity.noContent().build();
    }
}
