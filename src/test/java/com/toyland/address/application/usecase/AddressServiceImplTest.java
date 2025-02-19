package com.toyland.address.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.toyland.address.model.entity.Address;
import com.toyland.address.model.repository.AddressRepository;
import com.toyland.address.presentation.dto.request.AddressSearchRequestDto;
import com.toyland.address.presentation.dto.request.CreateAddressRequestDto;
import com.toyland.address.presentation.dto.response.AddressResponseDto;
import com.toyland.address.presentation.dto.response.AddressSearchResponseDto;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.global.exception.CustomException;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @DisplayName("주소 동적 쿼리 테스트")
    @Test
    @Transactional
    void dynamicSearch_Address() {
        // given
        User owner = userRepository.save(createMaster("홍길동"));

        Region seoulRegion = regionRepository.save(Region.builder()
            .regionName("서울시").build());

        Region busanRegion = regionRepository.save(Region.builder()
            .regionName("부산시").build());

        List<Address> addressList1 = IntStream.rangeClosed(1, 10)
            .mapToObj(i -> Address.builder()
                .addressName("서울 용산구 한강대로 " + (405 + i - 1))
                .region(seoulRegion)
                .user(owner)
                .build())
            .collect(Collectors.toList());

        List<Address> addressList2 = IntStream.rangeClosed(1, 10)
            .mapToObj(i -> Address.builder()
                .addressName("부산 동구 중앙대로 " + (206 + i - 1))
                .region(busanRegion)
                .user(owner)
                .build())
            .collect(Collectors.toList());

        addressRepository.saveAll(addressList1);
        addressRepository.saveAll(addressList2);
        // when

        //검색 조건1
        AddressSearchRequestDto case1 = new AddressSearchRequestDto("서울");
        //검색 조건2
        AddressSearchRequestDto case2 = new AddressSearchRequestDto("부산");
        //페이징 조건1
        Pageable pageable1 = PageRequest.of(0, 5, Sort.by("createdAt").ascending());
        //페이징 조건2
        Pageable pageable2 = PageRequest.of(1, 5, Sort.by("createdAt").ascending());

        Page<AddressSearchResponseDto> result1 = addressRepository.searchAddress(
            case1, pageable1);

        Page<AddressSearchResponseDto> result2 = addressRepository.searchAddress(
            case2, pageable2);

        // then
        //case1 검증
        assertThat(result1).isNotEmpty();
        assertThat(result1.getTotalElements()).isEqualTo(10);
        assertThat(result1.getTotalPages()).isEqualTo(2);
        assertThat(result1.getContent().get(0).addressName()).isEqualTo("서울 용산구 한강대로 405");
        assertThat(result1.getContent().get(4).addressName()).isEqualTo("서울 용산구 한강대로 409");

        //case2 검증
        assertThat(result2).isNotEmpty();
        assertThat(result2.getTotalElements()).isEqualTo(10);
        assertThat(result1.getTotalPages()).isEqualTo(2);
        assertThat(result2.getContent().get(0).addressName()).isEqualTo("부산 동구 중앙대로 211");
        assertThat(result2.getContent().get(4).addressName()).isEqualTo("부산 동구 중앙대로 215");
    }

    private User createMaster(String username) {
        return new User(username, "password", UserRoleEnum.MASTER);
    }


}