package com.toyland.store.infrastructure.impl;

import static com.toyland.region.model.entity.QRegion.region;
import static com.toyland.store.model.entity.QStore.store;
import static com.toyland.user.model.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.store.infrastructure.JpaStoreRepositoryCustom;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.dto.SearchStoreRepositoryRequestDto;
import com.toyland.store.presentation.dto.StoreWithOwnerResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class JpaStoreRepositoryCustomImpl implements JpaStoreRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<StoreWithOwnerResponseDto> searchStore(SearchStoreRepositoryRequestDto dto) {
    List<Store> storeList = queryFactory
        .select(store)
        .from(store)
        .innerJoin(store.region, region).fetchJoin()
        .innerJoin(store.owner, user).fetchJoin()
        .where(
            dto.getContainsSearchText(),
            dto.getEqOwnerId(),
            dto.getEqRegionId()
        )
        .orderBy(dto.orderSpecifiers())
        .offset(dto.offset())
        .limit(dto.size())
        .fetch();

    long totalCount = queryFactory
        .select(store.count())
        .from(store)
        .innerJoin(store.region, region)
        .innerJoin(store.owner, user)
        .where(
            dto.getContainsSearchText(),
            dto.getEqOwnerId(),
            dto.getEqRegionId()
        )
        .fetchOne();

    List<StoreWithOwnerResponseDto> list = storeList.stream().map(StoreWithOwnerResponseDto:: from)
        .collect(Collectors.toUnmodifiableList());
    return new PageImpl<>(list, PageRequest.of(dto.page(), dto.size()), totalCount);
  }
}
