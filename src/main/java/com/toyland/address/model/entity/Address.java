package com.toyland.address.model.entity;

import com.toyland.address.presentation.dto.CreateAddressRequestDto;
import com.toyland.region.model.entity.Region;
import com.toyland.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@Getter
@Entity
@Table(name = "p_address")
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID id;

    @Column(name = "address_name", nullable = false,length = 100)
    private String addressName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;


    //테스트나 null 허용하는 곳에서 사용
    @Builder
    public Address(String addressName, User user,Region region) {
        this.addressName = addressName;
        this.user = user;
    }
    //Dto를 통해 생성하는 Address의 비즈니스 로직에선 아래 정적 메서드 사용
    public static Address of(CreateAddressRequestDto dto, User user, Region region) {
        return Address.builder()
                .addressName(dto.addressName())
                .user(user)
                .region(region)
                .build();
    }

}
