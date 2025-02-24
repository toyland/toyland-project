/**
 * @Date : 2025. 02. 23.
 * @author : jieun(je-pa)
 */
package com.toyland.store.infrastructure;

import com.toyland.store.infrastructure.impl.dao.StoreWithOwnerResponseDao;
import com.toyland.store.model.repository.command.SearchStoreRepositoryCommand;
import org.springframework.data.domain.Page;

public interface JpaStoreRepositoryCustom {

  Page<StoreWithOwnerResponseDao> searchStore(SearchStoreRepositoryCommand build);
}
