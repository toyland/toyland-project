package com.toyland.storecategory.model.repository;

import com.toyland.store.model.entity.Store;
import com.toyland.storecategory.model.entity.StoreCategory;
import java.util.List;

public interface StoreCategoryRepository {

  <S extends StoreCategory> Iterable<S> saveAll(Iterable<S> entities);

  List<StoreCategory> findAllByStore(Store ids);

  void deleteByStore(Store entities);


  // test code 용
  void deleteAllInBatch();

  List<StoreCategory> findAll();
}
