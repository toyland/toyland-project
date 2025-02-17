/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import com.toyland.global.exception.type.domain.RegionErrorCode;
import com.toyland.global.exception.type.domain.UserErrorCode;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.user.model.User;
import com.toyland.user.model.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
  private final StoreRepository storeRepository;
  private final RegionRepository regionRepository;
  private final UserRepository userRepository;

  @Override
  public void createStore(CreateStoreRequestDto dto) {
    Region region = findRegionById(dto.regionId());
    User owner = findUserById(dto.ownerId());
    storeRepository.save(Store.of(dto, region, owner));
  }

  @Override
  public Store readStore(UUID id) {
    return storeRepository.findById(id)
        .orElseThrow(
            () -> CustomException.from(ProductErrorCode.NOT_FOUND)
        );
  }

  private Region findRegionById(UUID id) {
    return regionRepository.findById(id)
        .orElseThrow(
            () -> CustomException.from(RegionErrorCode.REGION_NOT_FOUND)
        );
  }

  private User findUserById(Long id){
    return userRepository.findById(id)
        .orElseThrow(
            () -> CustomException.from(UserErrorCode.USER_NOT_FOUND)
        );
  }
}
