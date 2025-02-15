/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import com.toyland.store.presentation.dto.CreateStoreRequestDto;

public interface StoreService {

  void createStore(CreateStoreRequestDto request);
}
