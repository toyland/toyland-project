/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import com.toyland.store.application.usecase.dto.CreateStoreCategoryListServiceRequestDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import java.util.UUID;

public interface StoreService {

  void createStore(CreateStoreRequestDto request);

  Store readStore(UUID id);

  void setStoreCategories(CreateStoreCategoryListServiceRequestDto dto);
}
