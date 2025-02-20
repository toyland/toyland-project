/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.model.repository;

import com.toyland.store.model.entity.Store;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository {

    Store save(Store from);

    Optional<Store> findById(UUID id);

    // test code 용
    List<Store> findAll();

    <S extends Store> Iterable<S> saveAll(Iterable<S> entities);

}
