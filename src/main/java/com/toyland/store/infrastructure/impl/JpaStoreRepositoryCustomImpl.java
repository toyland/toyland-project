package com.toyland.store.infrastructure.impl;

import static com.toyland.category.model.entity.QCategory.category;
import static com.toyland.region.model.entity.QRegion.region;
import static com.toyland.store.model.entity.QStore.store;
import static com.toyland.storecategory.model.entity.QStoreCategory.storeCategory;
import static com.toyland.user.model.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.store.infrastructure.JpaStoreRepositoryCustom;
import com.toyland.store.infrastructure.impl.dao.QStoreWithOwnerResponseDao;
import com.toyland.store.infrastructure.impl.dao.QStoreWithOwnerResponseDao_CategoryDao;
import com.toyland.store.infrastructure.impl.dao.QStoreWithOwnerResponseDao_OwnerDao;
import com.toyland.store.infrastructure.impl.dao.QStoreWithOwnerResponseDao_RegionDao;
import com.toyland.store.infrastructure.impl.dao.StoreWithOwnerResponseDao;
import com.toyland.store.model.repository.command.SearchStoreRepositoryCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class JpaStoreRepositoryCustomImpl implements JpaStoreRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<StoreWithOwnerResponseDao> searchStore(SearchStoreRepositoryCommand command) {
    List<StoreWithOwnerResponseDao> storeList = queryFactory
        .select(new QStoreWithOwnerResponseDao(
            store.id,
            store.name,
            store.content,
            store.address,
            store.avgRating,
            new QStoreWithOwnerResponseDao_RegionDao(
                region.id,
                region.regionName
            ),
            new QStoreWithOwnerResponseDao_OwnerDao(
                user.id,
                user.username
            ),
            new QStoreWithOwnerResponseDao_CategoryDao(
                category.id,
                category.name
            )
        ))
        .from(store)
        .innerJoin(store.region, region)
        .innerJoin(store.owner, user)
        .leftJoin(storeCategory).on(storeCategory.store.eq(store))
        .leftJoin(category).on(storeCategory.category.eq(category))
        .where(
            command.getContainsSearchText(),
            command.getContainsCategorySearchText(),
            command.getContainsStoreNameSearchText(),
            command.getEqOwnerId(),
            command.getEqRegionId(),
            command.getEqCategoryId()
        )
        .orderBy(command.orderSpecifiers())
        .offset(command.offset())
        .limit(command.size())
        .fetch();

    long totalCount = queryFactory
        .select(store.count())
        .from(store)
        .innerJoin(store.region, region)
        .innerJoin(store.owner, user)
        .leftJoin(storeCategory).on(storeCategory.store.eq(store))
        .leftJoin(category).on(storeCategory.category.eq(category))
        .where(
            command.getContainsSearchText(),
            command.getContainsCategorySearchText(),
            command.getContainsStoreNameSearchText(),
            command.getEqOwnerId(),
            command.getEqRegionId(),
            command.getEqCategoryId()
        )
        .fetchOne();

    return new PageImpl<>(storeList, PageRequest.of(command.page(), command.size()), totalCount);
  }
}
