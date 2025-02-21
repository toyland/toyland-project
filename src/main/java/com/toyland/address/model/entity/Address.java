package com.toyland.address.model.entity;

import com.toyland.address.presentation.dto.request.CreateAddressRequestDto;
import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.region.model.entity.Region;
import com.toyland.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@Getter
@Entity
@Table(name = "p_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID id;

    @Column(name = "address_name", nullable = false, length = 100)
    private String addressName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;


    @Builder
    public Address(UUID id, String addressName, User user, Region region) {
        this.id = id;
        this.addressName = addressName;
        this.user = user;
        this.region = region;
    }

    //연관관계 편의메소드
    public void joinUser(User user) {
        this.user = user;
        user.getAddressList().add(this);
    }

    //Dto를 통해 생성하는 Address의 비즈니스 로직에선 아래 정적 메서드 사용
    public static Address of(CreateAddressRequestDto dto, User user, Region region) {
        return Address.builder()
            .addressName(dto.addressName())
            .user(user)
            .region(region)
            .build();
    }

    //update 메서드
    public void updateAddress(CreateAddressRequestDto requestDto) {
        this.addressName = requestDto.addressName();
    }

}
