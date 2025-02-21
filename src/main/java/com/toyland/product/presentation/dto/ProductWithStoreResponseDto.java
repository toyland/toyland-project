/**
 * @Date : 2025. 02. 21.
 * @author : jieun(je-pa)
 */
package com.toyland.product.presentation.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductWithStoreResponseDto (
  UUID productId, String productName, BigDecimal price, boolean isDisplay, StoreDto store
){
  @QueryProjection
  public ProductWithStoreResponseDto{}

  public record StoreDto(
      UUID storeId, String storeName, String content
      , String address, String regionName, String ownerName
  ){
    @QueryProjection
    public StoreDto{}
  }
}
