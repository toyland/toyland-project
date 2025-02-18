package com.toyland.address.presentation;

import com.toyland.address.application.facade.AddressFacade;
import com.toyland.address.presentation.dto.AddressResponseDto;
import com.toyland.address.presentation.dto.CreateAddressRequestDto;
import com.toyland.global.config.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(addressFacade.createAddress(dto, userDetails.getId()));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> findAddressByAddressId(@PathVariable UUID addressId) {
        return ResponseEntity.ok(addressFacade.findByAddressId(addressId));
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
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        addressFacade.deleteAddress(addressId, userDetails.getId());
    }
}
