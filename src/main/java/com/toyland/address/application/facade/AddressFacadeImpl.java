package com.toyland.address.application.facade;

import com.toyland.address.application.usecase.AddressService;
import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.request.CreateAddressRequestDto;
import com.toyland.address.presentation.dto.response.AddressResponseDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@RequiredArgsConstructor
@Component
public class AddressFacadeImpl implements AddressFacade {

    private final AddressService addressService;


    @Override
    public AddressResponseDto createAddress(CreateAddressRequestDto requestDto, Long userId) {
        return addressService.createAddress(requestDto, userId);
    }

    @Override
    public AddressResponseDto findByAddressId(UUID addressId) {
        return addressService.findByAddressId(addressId);
    }


    @Override
    public AddressResponseDto updateAddress(UUID addressId, CreateAddressRequestDto requestDto) {
        return addressService.updateAddress(addressId, requestDto);
    }

    @Override
    public void deleteAddress(UUID addressId, Long userId) {
        addressService.deleteAddress(addressId, userId);
    }

    @Override
    public Page<AddressSearchResponseDto> searchAddress(AddressSearchRequestDto requestDto,
        Pageable pageable) {
        return addressService.searchAddress(requestDto, pageable);
    }
}
