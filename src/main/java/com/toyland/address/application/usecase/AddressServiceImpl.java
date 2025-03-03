package com.toyland.address.application.usecase;

import com.toyland.address.model.entity.Address;
import com.toyland.address.model.repository.AddressRepository;
import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.request.CreateAddressRequestDto;
import com.toyland.address.presentation.dto.response.AddressResponseDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.AddressErrorCode;
import com.toyland.global.exception.type.domain.RegionErrorCode;
import com.toyland.global.exception.type.domain.UserErrorCode;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    @Override
    public AddressResponseDto createAddress(CreateAddressRequestDto requestDto, Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() ->
                CustomException.from(UserErrorCode.USER_NOT_FOUND));

        Region region = regionRepository.findById(requestDto.regionId()).orElseThrow(() ->
            CustomException.from(RegionErrorCode.REGION_NOT_FOUND));

        Address savedAddress = addressRepository.save(
            Address.of(requestDto, user, region));

        return AddressResponseDto.from(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDto findByAddressId(UUID addressId) {
        return AddressResponseDto.from(addressRepository.findById(addressId)
            .orElseThrow(() ->
                CustomException.from(AddressErrorCode.ADDRESS_NOT_FOUND)));
    }

    @Override
    public AddressResponseDto updateAddress(UUID addressId, CreateAddressRequestDto requestDto) {
        Address findAddress = addressRepository.findById(addressId)
            .orElseThrow(() ->
                CustomException.from(AddressErrorCode.ADDRESS_NOT_FOUND));
        findAddress.updateAddress(requestDto);
        return AddressResponseDto.from(findAddress);
    }

    @Override
    public void deleteAddress(UUID addressId, Long userId) {
        Address findAddress = addressRepository.findById(addressId)
            .orElseThrow(() ->
                CustomException.from(AddressErrorCode.ADDRESS_NOT_FOUND));
        findAddress.addDeletedField(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressSearchResponseDto> searchAddress(AddressSearchRequestDto requestDto,
        Pageable pageable) {
        return addressRepository.searchAddress(requestDto, pageable);
    }
}
