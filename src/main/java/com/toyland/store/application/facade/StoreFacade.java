/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.facade;

import com.toyland.store.presentation.dto.CreateStoreCategoryListRequestDto;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import java.time.LocalDateTime;
import java.util.UUID;

public interface StoreFacade {

  void createStore(CreateStoreRequestDto request);

  void setStoreCategories(CreateStoreCategoryListRequestDto request, UUID storeId, Long loginUserId,
      LocalDateTime eventTime);
}
