/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
  private final StoreRepository storeRepository;

  @Override
  public void createStore(CreateStoreRequestDto dto) {
    storeRepository.save(Store.from(dto));
  }

}
