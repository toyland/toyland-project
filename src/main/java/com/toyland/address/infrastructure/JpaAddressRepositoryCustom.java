package com.toyland.address.infrastructure;

import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 19.
 */
public interface JpaAddressRepositoryCustom {

    Page<AddressSearchResponseDto> searchAddress(
        AddressSearchRequestDto requestDto, Pageable pageable);

}
