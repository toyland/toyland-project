package com.toyland.address.application.usecase;

import com.toyland.address.model.entity.Address;
import com.toyland.address.presentation.dto.AddressResponseDto;
import com.toyland.address.presentation.dto.CreateAddressRequestDto;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public interface AddressService {

    AddressResponseDto createAddress(CreateAddressRequestDto requestDto);

    Address findByAddressId(UUID addressId);
}
