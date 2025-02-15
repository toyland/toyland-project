/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.model.repository;

import com.toyland.store.model.entity.Store;
import java.util.List;

public interface StoreRepository {

  Store save(Store from);

  void deleteAllInBatch();

  List<Store> findAll();
}
