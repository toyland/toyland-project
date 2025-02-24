/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import com.toyland.store.application.usecase.dto.CreateStoreCategoryListServiceRequestDto;
import com.toyland.store.application.usecase.dto.DeleteStoreServiceRequestDto;
import com.toyland.store.application.usecase.dto.UpdateStoreServiceRequestDto;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.store.presentation.dto.SearchStoreRequestDto;
import com.toyland.store.presentation.dto.StoreResponseDto;
import com.toyland.store.presentation.dto.StoreWithOwnerResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface StoreService {

  StoreResponseDto createStore(CreateStoreRequestDto request);

  StoreResponseDto readStore(UUID id);

  Page<StoreWithOwnerResponseDto> searchStores(SearchStoreRequestDto request);

  StoreResponseDto updateStore(UpdateStoreServiceRequestDto dto);

  void deleteStore(DeleteStoreServiceRequestDto dto);

  void setStoreCategories(CreateStoreCategoryListServiceRequestDto dto);
}
