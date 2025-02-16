package com.toyland.region.infrastructure;

import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public interface JpaRegionRepository extends RegionRepository, JpaRepository<Region, UUID> {
}
