package com.toyland.address.model.entity;

import com.toyland.address.presentation.dto.CreateAddressRequestDto;
import com.toyland.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    //테스트나 null 허용하는 곳에서 사용
    @Builder
    public Address(String addressName, User user) {
        this.addressName = addressName;
        this.user = user;
    }
    //Dto를 통해 생성하는 Address의 비즈니스 로직에선 아래 정적 메서드 사용
    public static Address build(CreateAddressRequestDto dto, User user) {
        return Address.builder()
                .addressName(dto.addressName())
                .user(user)
                .build();
    }

}
