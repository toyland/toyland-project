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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


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


    @DisplayName("생성된 User가 자신의 주소를 생성하고 검증")
    @Test
    @Transactional
    void createAddress_Success_Test() {
        //given
        User user = new User("테스터", "1234", UserRoleEnum.CUSTOMER);
        userRepository.save(user);
        Region region = new Region("테스트 지역");
        regionRepository.save(region);

        //when
        AddressResponseDto createdAddress = addressService.createAddress(
            new CreateAddressRequestDto("경기도 성남시 분당구 정자일로 95", region.getId()), user.getId());
        AddressResponseDto findByAddress = addressService.findByAddressId(
            createdAddress.addressId());

        //then
        assertThat(createdAddress.addressName()).isEqualTo("경기도 성남시 분당구 정자일로 95");
        assertThat(createdAddress.addressId()).isEqualTo(findByAddress.addressId());

    }

    @DisplayName("정상적인 User가 아니여서 실패 하는 케이스")
    @Test
    @Transactional
    void invalidUser_fail_Test() {
        //given
        Long invalidUser = 123L;
        Region region = new Region("테스트 지역");
        regionRepository.save(region);
        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto(
            "경기도 성남시 분당구 정자일로 95", region.getId());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            addressService.createAddress(createAddressRequestDto, invalidUser);
        });

        //then
        assertEquals("존재하지 않는 사용자입니다.", exception.getMessage());

    }

    @DisplayName("addressId로 자신의 주소 조회를 실패하는 테스트")
    @Test
    @Transactional
    void findByAddressId_Fail_Test() {
        //given
        User user = new User("테스터", "1234", UserRoleEnum.CUSTOMER);
        userRepository.save(user);
        Region region = new Region("테스트 지역");
        regionRepository.save(region);

        //when
        addressService.createAddress(
            new CreateAddressRequestDto("경기도 성남시 분당구 정자일로 95", region.getId()), user.getId());
        CustomException exception = assertThrows(CustomException.class, () -> {
            addressService.findByAddressId(UUID.fromString("6fa2bafc-a7d3-4597-abaf-92630b435cdc"));
        });

        //then
        assertEquals("존재하지 않는 주소 입니다. 올바른 주소 ID를 입력해주세요.", exception.getMessage());

    }

    @DisplayName("Address 단 건 조회 테스트")
    @Test
    @Transactional
    void find_By_Address() {
        //given
        User user = new User("테스터", "1234", UserRoleEnum.CUSTOMER);
        userRepository.save(user);
        Region region = new Region("테스트 지역");
        regionRepository.save(region);

        //when
        AddressResponseDto createdAddress = addressService.createAddress(
            new CreateAddressRequestDto("경기도 성남시 분당구 정자일로 95", region.getId()), user.getId());
        AddressResponseDto findByAddress = addressService.findByAddressId(
            createdAddress.addressId());

        //then
        assertThat(createdAddress.addressId()).isEqualTo(findByAddress.addressId());
        assertThat(createdAddress.addressName()).isEqualTo(findByAddress.addressName());

    }


    @DisplayName("Address 수정 테스트")
    @Test
    @Transactional
    void update_By_Address() {
        //given
        User user = new User("테스터", "1234", UserRoleEnum.CUSTOMER);
        User savedUser = userRepository.save(user);
        Region region = new Region("테스트 지역");
        Region savedRegion = regionRepository.save(region);
        CreateAddressRequestDto updateDto = new CreateAddressRequestDto("수정",
            savedRegion.getId());

        //when
        AddressResponseDto createdAddress = addressService.createAddress(
            new CreateAddressRequestDto("경기도 성남시 분당구 정자일로 95", region.getId()), user.getId());
        AddressResponseDto updatedAddress = addressService.updateAddress(
            createdAddress.addressId(), updateDto);

        //then
        assertThat(updatedAddress.addressName()).isEqualTo(updateDto.addressName());

    }

    @DisplayName("Address 삭제 테스트")
    @Test
    void delete_By_Address() {
        //given
        User user = new User("테스터", "1234", UserRoleEnum.CUSTOMER);
        User savedUser = userRepository.save(user);
        Region region = new Region("테스트 지역");
        regionRepository.save(region);

        //when
        AddressResponseDto createdAddress = addressService.createAddress(
            new CreateAddressRequestDto("경기도 성남시 분당구 정자일로 95", region.getId()), user.getId());

        AddressResponseDto deleteBeforeAddress = addressService.findByAddressId(
            createdAddress.addressId());

        addressService.deleteAddress(createdAddress.addressId(), savedUser.getId());

        // then 삭제 후 조회하면 예외가 발생해야 함
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            addressRepository.findById(createdAddress.addressId())
                .orElseThrow(() -> new RuntimeException("삭제된 주소 입니다."));
        });

        assertThat(exception.getMessage()).isEqualTo("삭제된 주소 입니다.");
        //삭제 이전에 정상 조회 되는지 검증
        assertThat(deleteBeforeAddress.addressName()).isEqualTo(createdAddress.addressName());
    }


}