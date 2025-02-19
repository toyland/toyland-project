package com.toyland.region.model.entity;

import com.toyland.address.model.entity.Address;
import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.region.presentation.dto.repuest.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.response.RegionResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Getter
@Entity
@Table(name = "p_region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "region_id")
    private UUID id;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;


    @OneToMany(mappedBy = "region")
    private List<Address> addressList = new ArrayList<>();

    //오버로딩
    public static Region from(CreateRegionRequestDto dto) {
        return Region.builder()
            .regionName(dto.regionName())
            .build();
    }

    //오버로딩
    public static Region from(RegionResponseDto dto) {
        return Region.builder()
            .regionName(dto.regionName())
            .build();
    }

    @Builder
    public Region(UUID id, String regionName) {
        this.id = id;
        this.regionName = regionName;
    }

    //update 메서드
    public void updateRegion(String regionName) {
        this.regionName = regionName;
    }

    //테스트 생성자
    public Region(String regionName) {
        this.regionName = regionName;
    }
}
