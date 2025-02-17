package com.toyland.address.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.toyland.address.model.repository.AddressRepository;
import com.toyland.address.presentation.dto.AddressResponseDto;
import com.toyland.address.presentation.dto.CreateAddressRequestDto;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.global.exception.CustomException;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author : hanjihoon
 * @Date : 2025. 02. 15.
 */
class AddressServiceImplTest extends IntegrationTestSupport {

    @Autowired
    AddressService addressService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    RegionRepository regionRepository;

    @Autowired
    UserRepository userRepository;

    //테스트 케이스 하나 만들고 구성하면 유지보수 쉬울 것 같음
    @AfterEach
    void tearDown() {
        addressRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        regionRepository.deleteAllInBatch();
    }

    @DisplayName("생성된 User가 자신의 주소를 생성하고 검증")
    @Test
    void createAddress_Success_Test() {
        //given
        User user = new User("테스터", "1234", UserRoleEnum.CUSTOMER);
        userRepository.save(user);
        Region region = new Region("테스트 지역");
        regionRepository.save(region);

        //when
        AddressResponseDto createdAddress = addressService.createAddress(
            new CreateAddressRequestDto("경기도 성남시 분당구 정자일로 95", user.getId(), region.getId()));
        AddressResponseDto findByAddress = addressService.findByAddressId(
            createdAddress.addressId());

        //then
        assertThat(createdAddress.addressName()).isEqualTo("경기도 성남시 분당구 정자일로 95");
        assertThat(createdAddress.addressId()).isEqualTo(findByAddress.addressId());

    }

    @DisplayName("정상적인 User가 아니여서 실패 하는 케이스")
    @Test
    void invalidUser_fail_Test() {
        //given
        Long invalidUser = 123L;
        Region region = new Region("테스트 지역");
        regionRepository.save(region);
        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto(
            "경기도 성남시 분당구 정자일로 95", invalidUser, region.getId());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            addressService.createAddress(createAddressRequestDto);
        });

        //then
        assertEquals("존재하지 않는 사용자입니다.", exception.getMessage());

    }

    @DisplayName("addressId로 자신의 주소 조회를 실패하는 테스트")
    @Test
    void findByAddressId_Fail_Test() {
        //given
        User user = new User("테스터", "1234", UserRoleEnum.CUSTOMER);
        userRepository.save(user);
        Region region = new Region("테스트 지역");
        regionRepository.save(region);

        //when
        AddressResponseDto createdAddress = addressService.createAddress(
            new CreateAddressRequestDto("경기도 성남시 분당구 정자일로 95", user.getId(), region.getId()));
        CustomException exception = assertThrows(CustomException.class, () -> {
            addressService.findByAddressId(UUID.fromString("6fa2bafc-a7d3-4597-abaf-92630b435cdc"));
        });

        //then
        assertEquals("존재하지 않는 주소 입니다. 올바른 주소 ID를 입력해주세요.", exception.getMessage());

    }


}