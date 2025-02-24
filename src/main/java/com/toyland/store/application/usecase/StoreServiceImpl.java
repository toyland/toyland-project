/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import com.toyland.category.model.entity.Category;
import com.toyland.category.model.repository.CategoryRepository;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.CategoryErrorCode;
import com.toyland.global.exception.type.domain.RegionErrorCode;
import com.toyland.global.exception.type.domain.StoreErrorCode;
import com.toyland.global.exception.type.domain.UserErrorCode;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.store.application.usecase.dto.CreateStoreCategoryListServiceRequestDto;
import com.toyland.store.application.usecase.dto.DeleteStoreServiceRequestDto;
import com.toyland.store.application.usecase.dto.UpdateStoreServiceRequestDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.store.model.repository.command.SearchStoreRepositoryCommand;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.store.presentation.dto.SearchStoreRequestDto;
import com.toyland.store.presentation.dto.StoreResponseDto;
import com.toyland.store.presentation.dto.StoreWithOwnerResponseDto;
import com.toyland.storecategory.model.entity.StoreCategory;
import com.toyland.storecategory.model.repository.StoreCategoryRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
  private final StoreRepository storeRepository;
  private final RegionRepository regionRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final StoreCategoryRepository storeCategoryRepository;

  @Override
  public StoreResponseDto createStore(CreateStoreRequestDto dto) {
    Region region = findRegionById(dto.regionId());
    User owner = findUserById(dto.ownerId());
    return StoreResponseDto.from(storeRepository.save(Store.of(dto, region, owner)));
  }

  @Override
  @Transactional(readOnly = true)
  public StoreResponseDto readStore(UUID id) {
    return StoreResponseDto.from(findStoreById(id));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<StoreWithOwnerResponseDto> searchStores(SearchStoreRequestDto dto) {
    return storeRepository.searchStore(
        SearchStoreRepositoryCommand.builder()
            .searchText(dto.searchText())
            .categoryNameSearchText(dto.categoryNameSearchText())
            .storeNameSearchText(dto.storeNameSearchText())
            .regionId(dto.regionId())
            .ownerId(dto.ownerId())
            .categoryId(dto.categoryId())
            .page(dto.page() - 1)
            .size(Set.of(10, 30, 50).contains(dto.size()) ? dto.size() : 10)
            .sort(dto.sort())
            .build()
    ).map(StoreWithOwnerResponseDto::from);
  }

  @Override
  @Transactional
  public StoreResponseDto updateStore(UpdateStoreServiceRequestDto dto) {
    Store store = findStoreById(dto.id());
    User owner = dto.ownerId() == null ? null : findUserById(dto.ownerId());
    Region region = dto.regionId() == null ? null : findRegionById(dto.regionId());

    store.updateStore(dto.name(), dto.name(), dto.address(), owner, region);

    return StoreResponseDto.from(store);
  }

  @Override
  @Transactional
  public void deleteStore(DeleteStoreServiceRequestDto dto) {
    Store store = findStoreById(dto.storeId());
    store.delete(dto.eventDateTime(), dto.actorId());
  }

  @Override
  public void setStoreCategories(CreateStoreCategoryListServiceRequestDto dto) {
    Store store = findStoreById(dto.storeId());

    deleteAllByStore(dto.actorId(), dto.eventTime(), store);

    List<Category> categories = getCategories(dto);

    List<StoreCategory> storeCategories = categories.stream()
        .map(category -> new StoreCategory(store, category))
        .collect(Collectors.toList());
    storeCategoryRepository.saveAll(storeCategories);
  }

  private void deleteAllByStore(Long actorId, LocalDateTime eventTime, Store store) {
    List<StoreCategory> before = storeCategoryRepository.findAllByStore(store);
    before.stream().forEach((a)->a.delete(eventTime, actorId));
  }

  private Store findStoreById(UUID storeId) {
    return storeRepository.findById(storeId).orElseThrow(
        () -> CustomException.from(StoreErrorCode.STORE_NOT_FOUND)
    );
  }

  private List<Category> getCategories(CreateStoreCategoryListServiceRequestDto dto) {
    if(dto.existsDuplicateCategoryId()) {
      throw CustomException.from(CategoryErrorCode.ID_DUPLICATE);
    }
    return categoryRepository.findAllById(dto.categoryIdList());
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
