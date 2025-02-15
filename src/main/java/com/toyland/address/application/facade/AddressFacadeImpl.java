package com.toyland.address.application.facade;

import com.toyland.address.application.usecase.AddressService;
import com.toyland.address.model.entity.Address;
import com.toyland.address.presentation.dto.AddressResponseDto;
import com.toyland.address.presentation.dto.CreateAddressRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@RequiredArgsConstructor
@Component
public class AddressFacadeImpl implements AddressFacade{

    private final AddressService addressService;


    @Override
    public AddressResponseDto createAddress(CreateAddressRequestDto requestDto) {
        return addressService.createAddress(requestDto);
    }

    @Override
    public Address findByAddressId(UUID addressId) {
        return addressService.findByAddressId(addressId);
    }
}
