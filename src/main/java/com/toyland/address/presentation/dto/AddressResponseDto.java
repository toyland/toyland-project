package com.toyland.address.presentation.dto;

import com.toyland.address.model.entity.Address;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public record AddressResponseDto(UUID addressId,
                                 String addressName,
                                 Long userId) {


    public static AddressResponseDto from(Address address) {
        return new AddressResponseDto(address.getId(),
                address.getAddressName(),
                address.getUser().getId());
    }

}
