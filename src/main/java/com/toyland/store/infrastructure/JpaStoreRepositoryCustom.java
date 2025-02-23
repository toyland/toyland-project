/**
 * @Date : 2025. 02. 23.
 * @author : jieun(je-pa)
 */
package com.toyland.store.infrastructure;

import com.toyland.store.model.repository.dto.SearchStoreRepositoryRequestDto;
import com.toyland.store.presentation.dto.StoreWithOwnerResponseDto;
import org.springframework.data.domain.Page;

public interface JpaStoreRepositoryCustom {

  Page<StoreWithOwnerResponseDto> searchStore(SearchStoreRepositoryRequestDto build);
}
