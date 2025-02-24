package com.toyland.region.infrastructure.Impl;


import static com.toyland.region.model.entity.QRegion.region;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyland.region.infrastructure.JpaRegionRepositoryCustom;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.command.SearchRegionRepositoryCommand;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
@RequiredArgsConstructor
@Slf4j
public class JpaRegionRepositoryCustomImpl implements JpaRegionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RegionSearchResponseDto> searchRegion(SearchRegionRepositoryCommand command) {

        List<Region> fetch = queryFactory
            .selectFrom(region)
            .where(
                command.regionNameContains()
            )
            .orderBy(command.orderSpecifiers())
            .offset(command.offset())
            .limit(command.size())
            .distinct()
            .fetch();

        // RegionSearchResponseDto로 변환
        List<RegionSearchResponseDto> regionSearchResponseDtos = fetch.stream()
            .map(RegionSearchResponseDto::from)
            .collect(Collectors.toList());

        Long totalCount = queryFactory
            .select(region.count())
            .from(region)
            .where(
                command.regionNameContains()
            )
            .fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(regionSearchResponseDtos,
            PageRequest.of(command.page(), command.size()), totalCount);

    }

}
