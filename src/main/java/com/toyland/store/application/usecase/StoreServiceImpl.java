/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import java.util.UUID;
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

  @Override
  public Store readStore(UUID id) {
    return storeRepository.findById(id)
        .orElseThrow(
            () -> CustomException.from(ProductErrorCode.NOT_FOUND)
        );
  }

}
