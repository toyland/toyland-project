/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.infrastructure;

import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStoreRepository extends StoreRepository, JpaRepository<Store, UUID>, JpaStoreRepositoryCustom {

}
