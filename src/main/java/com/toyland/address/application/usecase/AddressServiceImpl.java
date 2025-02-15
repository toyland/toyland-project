package com.toyland.address.application.usecase;

import com.toyland.address.model.entity.Address;
import com.toyland.address.model.repository.AddressRepository;
import com.toyland.address.presentation.dto.AddressResponseDto;
import com.toyland.address.presentation.dto.CreateAddressRequestDto;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.BusinessErrorCode;
import com.toyland.user.model.User;
import com.toyland.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    // userRepository 이 부분은 정길님이 UserService에서 findByUserId 만들면 AddressFacadeImpl에서
    // 가져오도록 리팩토링 예정
    private final UserRepository userRepository;
    @Override
    public AddressResponseDto createAddress(CreateAddressRequestDto requestDto) {
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new CustomException(BusinessErrorCode.USER_NOT_FOUND));

        Address savedAddress = addressRepository.save(Address.of(requestDto, user));

        return AddressResponseDto.from(savedAddress);
    }

    @Override
    public Address findByAddressId(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new CustomException(BusinessErrorCode.ADDRESS_NOT_FOUND));
    }
}
