package com.toyland.address.presentation;

import com.toyland.address.application.facade.AddressFacade;
import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.request.CreateAddressRequestDto;
import com.toyland.address.presentation.dto.response.AddressResponseDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressFacade addressFacade;


    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(
        @Valid @RequestBody CreateAddressRequestDto dto,
        @CurrentLoginUserId Long userId) {
        AddressResponseDto address = addressFacade.createAddress(dto, userId);
        return ResponseEntity.created(
                UriComponentsBuilder.fromUriString("/api/v1/{addressId}")
                    .buildAndExpand(address.addressId())
                    .toUri())
            .body(address);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> findAddressByAddressId(@PathVariable UUID addressId) {
        return ResponseEntity.ok(addressFacade.findByAddressId(addressId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AddressSearchResponseDto>> searchAddress(
        AddressSearchRequestDto requestDto, Pageable pageable) {

        return ResponseEntity.ok(addressFacade.searchAddress(requestDto, pageable));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> updateAddressByAddressId(
        @PathVariable UUID addressId,
        @RequestBody CreateAddressRequestDto requestDto
    ) {
        return ResponseEntity.ok(addressFacade.updateAddress(addressId, requestDto));
    }

    @DeleteMapping("/{addressId}")
    public void deleteAddressByAddressId(
        @PathVariable UUID addressId,
        @CurrentLoginUserId Long userId) {
        addressFacade.deleteAddress(addressId, userId);
    }
}
