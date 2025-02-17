package com.toyland.region.model.repository;

import com.toyland.region.model.entity.Region;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
public interface RegionRepository {

    Region save(Region region);

    Optional<Region> findById(UUID regionId);

    void deleteById(UUID regionId);

    //테스트
    void deleteAllInBatch();
}
