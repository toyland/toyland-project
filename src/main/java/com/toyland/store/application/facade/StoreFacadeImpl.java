/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.facade;

import com.toyland.store.application.usecase.StoreService;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreFacadeImpl implements StoreFacade{
  private final StoreService storeService;

  @Override
  public void createStore(CreateStoreRequestDto request) {
    storeService.createStore(request);
  }
}
