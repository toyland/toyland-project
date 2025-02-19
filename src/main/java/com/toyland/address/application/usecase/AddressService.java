package com.toyland.address.application.usecase;

import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.request.CreateAddressRequestDto;
import com.toyland.address.presentation.dto.response.AddressResponseDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public interface AddressService {

    AddressResponseDto createAddress(CreateAddressRequestDto requestDto, Long userId);

    AddressResponseDto findByAddressId(UUID addressId);

    AddressResponseDto updateAddress(UUID addressId, CreateAddressRequestDto requestDto);

    void deleteAddress(UUID addressId, Long userId);

    Page<AddressSearchResponseDto> searchAddress(
        AddressSearchRequestDto requestDto, Pageable pageable);
}
