package com.toyland.region.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.region.application.facade.RegionFacade;
import com.toyland.region.presentation.dto.repuest.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionResponseDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionFacade regionFacade;

    //권한이 둘 중 하나라도 일치하면 true
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @PostMapping
    public ResponseEntity<RegionResponseDto> createRegion(
        @RequestBody CreateRegionRequestDto requestDto) {
        RegionResponseDto region = regionFacade.createRegion(requestDto);

        return ResponseEntity.created(
                UriComponentsBuilder.fromUriString("/api/v1/{regionId}")
                    .buildAndExpand(region.regionId())
                    .toUri())
            .build();
    }

    @GetMapping("/{regionId}")
    public ResponseEntity<RegionResponseDto> findRegionByRegionId(@PathVariable UUID regionId) {
        return ResponseEntity.ok(regionFacade.findByRegionId(regionId));
    }

    @GetMapping("/search")
    public Page<RegionSearchResponseDto> searchRegion(RegionSearchRequestDto searchRequestDto,
        Pageable pageable) {
        return regionFacade.searchRegion(searchRequestDto, pageable);
    }


    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_MASTER')")
    @PutMapping("/{regionId}")
    public ResponseEntity<RegionResponseDto> updateRegionByRegionId(@PathVariable UUID regionId,
        @RequestBody CreateRegionRequestDto requestDto) {
        return ResponseEntity.ok(regionFacade.updateRegion(regionId, requestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_MASTER')")
    @DeleteMapping("/{regionId}")
    public void deleteRegionByRegionId(@PathVariable UUID regionId,
        @CurrentLoginUserId Long userId) {
        regionFacade.deleteByRegionId(regionId, userId);

    }
}
