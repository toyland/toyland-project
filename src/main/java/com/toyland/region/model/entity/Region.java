package com.toyland.region.model.entity;

import com.toyland.address.model.entity.Address;
import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.region.presentation.dto.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.RegionResponseDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Getter
@Entity
@Table(name = "p_region")
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "region_id")
    private UUID id;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;

    @Builder.Default
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

    //update 메서드
    public void updateRegion(String regionName) {
        this.regionName = regionName;
    }

    //테스트 생성자
    public Region(String regionName) {
        this.regionName = regionName;
    }
}
