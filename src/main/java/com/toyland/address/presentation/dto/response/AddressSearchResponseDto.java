package com.toyland.address.presentation.dto.response;

import com.toyland.address.model.entity.Address;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 19.
 */
@Builder
public record AddressSearchResponseDto(UUID addressId,
                                       String addressName,
                                       Long createdBy,
                                       LocalDateTime createdAt,
                                       Long updatedBy,
                                       LocalDateTime updatedAt,
                                       Long deletedBy,
                                       LocalDateTime deletedAt) {


    public static AddressSearchResponseDto from(Address address) {
        return AddressSearchResponseDto.builder()
            .addressId(address.getId())
            .addressName(address.getAddressName())
            .createdAt(address.getCreatedAt())
            .createdBy(address.getCreatedBy())
            .updatedAt(address.getUpdatedAt())
            .updatedBy(address.getUpdatedBy())
            .deletedAt(address.getDeletedAt())
            .deletedBy(address.getDeletedBy())
            .build();


    }

}
