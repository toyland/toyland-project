/**
 * @Date : 2025. 02. 23.
 * @author : jieun(je-pa)
 */
package com.toyland.store.presentation.dto;

import com.toyland.region.model.entity.Region;
import com.toyland.store.model.entity.Store;
import com.toyland.user.model.User;
import java.util.UUID;
import lombok.Builder;

@Builder
public record StoreWithOwnerResponseDto(
    UUID storeId, String storeName, String storeContent, String storeAddress, Double avgRating
    , RegionDto region, OwnerDto owner
) {

  public static StoreWithOwnerResponseDto from(Store store) {
    return StoreWithOwnerResponseDto.builder()
        .storeId(store.getId())
        .storeName(store.getName())
        .storeContent(store.getContent())
        .storeAddress(store.getAddress())
        .avgRating(store.getAvgRating())
        .region(RegionDto.from(store.getRegion()))
        .owner(OwnerDto.from(store.getOwner()))
        .build();
  }

  @Builder
  public record RegionDto(
      UUID regionId, String regionName
  ) {

    public static RegionDto from(Region region) {
      if (region == null) {
        return null;
      }
      return RegionDto
          .builder()
          .regionId(region.getId())
          .regionName(region.getRegionName())
          .build();
    }
  }

  @Builder
  public record OwnerDto(
      Long ownerId, String ownerName
  ) {

    public static OwnerDto from(User user) {
      if (user == null) {
        return null;
      }
      return OwnerDto
          .builder()
          .ownerId(user.getId())
          .ownerName(user.getUsername())
          .build();
    }
  }
}
