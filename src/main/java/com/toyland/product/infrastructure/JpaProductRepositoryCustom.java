/**
 * @Date : 2025. 02. 21.
 * @author : jieun(je-pa)
 */
package com.toyland.product.infrastructure;

import com.toyland.product.presentation.dto.ProductWithStoreResponseDto;
import com.toyland.product.presentation.dto.SearchProductRequestDto;
import org.springframework.data.domain.Page;

public interface JpaProductRepositoryCustom {

  Page<ProductWithStoreResponseDto> searchProducts(SearchProductRequestDto dto);

}
