/**
 * @Date : 2025. 02. 23.
 * @author : jieun(je-pa)
 */
package com.toyland.store.presentation.dto;

import com.toyland.store.infrastructure.impl.dao.StoreWithOwnerResponseDao;
import com.toyland.store.infrastructure.impl.dao.StoreWithOwnerResponseDao.CategoryDao;
import com.toyland.store.infrastructure.impl.dao.StoreWithOwnerResponseDao.OwnerDao;
import com.toyland.store.infrastructure.impl.dao.StoreWithOwnerResponseDao.RegionDao;
import java.util.UUID;
import lombok.Builder;

@Builder
public record StoreWithOwnerResponseDto(
    UUID storeId, String storeName, String storeContent, String storeAddress, Double avgRating
    , RegionDto region, OwnerDto owner, CategoryDto category
) {

  public static StoreWithOwnerResponseDto from(StoreWithOwnerResponseDao dao) {
    return StoreWithOwnerResponseDto.builder()
        .storeId(dao.storeId())
        .storeName(dao.storeName())
        .storeContent(dao.storeContent())
        .storeAddress(dao.storeAddress())
        .avgRating(dao.avgRating())
        .region(RegionDto.from(dao.region()))
        .owner(OwnerDto.from(dao.owner()))
        .category(CategoryDto.from(dao.category()))
        .build();
  }

  @Builder
  public record RegionDto(
      UUID regionId, String regionName
  ) {

    public static RegionDto from(RegionDao region) {
      if (region == null) {
        return null;
      }
      return RegionDto
          .builder()
          .regionId(region.regionId())
          .regionName(region.regionName())
          .build();
    }
  }

  @Builder
  public record OwnerDto(
      Long ownerId, String ownerName
  ) {

    public static OwnerDto from(OwnerDao user) {
      if (user == null) {
        return null;
      }
      return OwnerDto
          .builder()
          .ownerId(user.ownerId())
          .ownerName(user.ownerName())
          .build();
    }
  }

  @Builder
  public record CategoryDto(
      UUID categoryId, String categoryName
  ){

    public static CategoryDto from(CategoryDao category) {
      if (category == null) {
        return null;
      }
      return CategoryDto.builder()
          .categoryId(category.categoryId())
          .categoryName(category.categoryName())
          .build();
    }
  }
}
