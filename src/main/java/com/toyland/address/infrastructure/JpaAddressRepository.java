package com.toyland.address.infrastructure;

import com.toyland.address.model.entity.Address;
import com.toyland.address.model.repository.AddressRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
public interface JpaAddressRepository extends AddressRepository, JpaRepository<Address, UUID>,
    JpaAddressRepositoryCustom {

}
