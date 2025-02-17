package com.toyland.address.application.facade;

import com.toyland.address.presentation.dto.AddressResponseDto;
import com.toyland.address.presentation.dto.CreateAddressRequestDto;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public interface AddressFacade {

    AddressResponseDto createAddress(CreateAddressRequestDto requestDto, Long userId);

    AddressResponseDto findByAddressId(UUID addressId);

    AddressResponseDto updateAddress(UUID addressId, CreateAddressRequestDto requestDto);

    void deleteAddress(UUID addressId, Long userId);
}
