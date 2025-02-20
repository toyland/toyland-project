package com.toyland.address.presentation.dto.response;

import com.toyland.address.model.entity.Address;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@Builder(access = AccessLevel.PROTECTED)
public record AddressResponseDto(UUID addressId,
                                 String addressName,
                                 Long userId) {


    public static AddressResponseDto from(Address address) {
        return AddressResponseDto.builder()
            .addressId(address.getId())
            .addressName(address.getAddressName())
            .userId(address.getUser().getId())
            .build();
    }

}
