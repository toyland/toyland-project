/**
 * @Date : 2025. 02. 23.
 * @author : jieun(je-pa)
 */
package com.toyland.store.infrastructure.impl.dao;

import com.querydsl.core.annotations.QueryProjection;
import java.util.UUID;
import lombok.Builder;

@Builder
public record StoreWithOwnerResponseDao(
    UUID storeId, String storeName, String storeContent, String storeAddress, Double avgRating
    , RegionDao region, OwnerDao owner, CategoryDao category
) {

  @QueryProjection
  public StoreWithOwnerResponseDao {
  }

  @Builder
  public record RegionDao(
      UUID regionId, String regionName
  ) {

    @QueryProjection
    public RegionDao {}

  }

  @Builder
  public record OwnerDao(
      Long ownerId, String ownerName
  ) {

    @QueryProjection
    public OwnerDao {}
  }

  @Builder
  public record CategoryDao(
      UUID categoryId, String categoryName
  ) {

    @QueryProjection
    public CategoryDao {
    }
  }
}
