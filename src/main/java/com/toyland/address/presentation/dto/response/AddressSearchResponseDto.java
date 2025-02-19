package com.toyland.address.presentation.dto.response;

import com.toyland.address.model.entity.Address;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 19.
 */
public record AddressSearchResponseDto(UUID addressId,
                                       String addressName,
                                       Long createdBy,
                                       LocalDateTime createdAt,
                                       Long updatedBy,
                                       LocalDateTime updatedAt,
                                       Long deletedBy,
                                       LocalDateTime deletedAt) {


    public static AddressSearchResponseDto from(Address address) {
        return new AddressSearchResponseDto(
            address.getId(),
            address.getAddressName(),
            address.getCreatedBy(),
            address.getCreatedAt(),
            address.getUpdatedBy(),
            address.getUpdatedAt(),
            address.getDeletedBy(),
            address.getDeletedAt()
        );

    }

}
