package com.toyland.address.model.repository;

import com.toyland.address.model.entity.Address;

import java.util.Optional;
import java.util.UUID;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public interface AddressRepository {
    Address save(Address address);

    Optional<Address> findById(UUID addressId);

    //테스트
    void deleteAll();

}
