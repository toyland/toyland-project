/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.facade;

import com.toyland.product.presentation.dto.CreateProductRequestDto;

public interface ProductFacade {

    void createProduct(CreateProductRequestDto dto);
}
