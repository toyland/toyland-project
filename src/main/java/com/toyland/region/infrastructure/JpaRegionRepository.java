package com.toyland.region.infrastructure;

import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Repository
public interface JpaRegionRepository extends RegionRepository, JpaRepository<Region, UUID>,
    JpaRegionRepositoryCustom {

}
