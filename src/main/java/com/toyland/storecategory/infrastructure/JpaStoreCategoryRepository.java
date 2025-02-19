package com.toyland.storecategory.infrastructure;

import com.toyland.storecategory.model.entity.StoreCategory;
import com.toyland.storecategory.model.repository.StoreCategoryRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStoreCategoryRepository extends StoreCategoryRepository, JpaRepository<StoreCategory, UUID> {

}
