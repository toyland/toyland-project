/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.facade;

import com.toyland.store.presentation.dto.CreateStoreRequestDto;

public interface StoreFacade {

  void createStore(CreateStoreRequestDto request);
}
