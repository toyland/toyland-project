package com.toyland.region.presentation;

import com.toyland.global.config.security.UserDetailsImpl;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionFacade regionFacade;

    @PostMapping
    public ResponseEntity<RegionResponseDto> createRegion(
        @RequestBody CreateRegionRequestDto requestDto) {
        RegionResponseDto region = regionFacade.createRegion(requestDto);
        return ResponseEntity.ok(region);
    }

    @GetMapping("/{regionId}")
    public ResponseEntity<RegionResponseDto> findRegionByRegionId(@PathVariable UUID regionId) {
        return ResponseEntity.ok(regionFacade.findByRegionId(regionId));
    }

    @GetMapping("/search")
    public Page<RegionSearchResponseDto> searchRegion(
        RegionSearchRequestDto searchRequestDto,
        Pageable pageable
    ) {
        return regionFacade.searchRegion(searchRequestDto, pageable);
    }


    @PutMapping("/{regionId}")
    public ResponseEntity<RegionResponseDto> updateRegionByRegionId(@PathVariable UUID regionId,
        @RequestBody CreateRegionRequestDto requestDto) {
        return ResponseEntity.ok(regionFacade.updateRegion(regionId, requestDto));
    }

    @DeleteMapping("/{regionId}")
    public void deleteRegionByRegionId(@PathVariable UUID regionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        regionFacade.deleteByRegionId(regionId, userDetails.getUser().getId());
    }
}
