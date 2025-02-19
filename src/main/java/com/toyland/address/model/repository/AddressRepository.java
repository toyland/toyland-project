package com.toyland.address.model.repository;

import com.toyland.address.model.entity.Address;
import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public interface AddressRepository {

    Address save(Address address);

    Optional<Address> findById(UUID addressId);


    Page<AddressSearchResponseDto> searchAddress(AddressSearchRequestDto requestDto,
        Pageable pageable);

    //테스트
    void deleteAllInBatch();

    <S extends Address> Iterable<S> saveAll(Iterable<S> entities);
}
