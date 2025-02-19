/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.facade;

import com.toyland.product.application.usecase.ProductService;
import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;
import com.toyland.product.presentation.dto.CreateProductRequestDto;
import com.toyland.store.application.usecase.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;
    private final StoreService storeService;

    @Override
    public void createProduct(CreateProductRequestDto dto) {
        productService.createProduct(
            CreateProductServiceRequestDto.of(
                dto, storeService.readStore(dto.storeId()))
        );
    }
}
