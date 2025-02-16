package com.toyland.region.model.entity;

import com.toyland.address.model.entity.Address;
import com.toyland.region.presentation.dto.CreateRegionRequestDto;
import jakarta.persistence.*;
import lombok.*;

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
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "region_id")
    private UUID id;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;

    @OneToMany(mappedBy = "region")
    private List<Address> addressList = new ArrayList<>();

    public static Region from(CreateRegionRequestDto dto) {
        return Region.builder()
                .regionName(dto.regionName())
                .build();
    }

    //테스트 생성자
    public Region(String regionName) {
        this.regionName = regionName;
    }
}
