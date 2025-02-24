package com.toyland.region.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.region.presentation.dto.repuest.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.repuest.RegionSearchRequestDto;
import com.toyland.region.presentation.dto.response.RegionResponseDto;
import com.toyland.region.presentation.dto.response.RegionSearchResponseDto;
import com.toyland.store.application.usecase.StoreService;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 17.
 */
class RegionServiceImplTest extends IntegrationTestSupport {

    @Autowired
    RegionRepository regionRepository;
    @Autowired
    RegionService regionService;
    @Autowired
    StoreService storeService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;


    @DisplayName("지역 단 건 조회 테스트")
    @Test
    void find_By_Region() {
        //given
        Region region = new Region("서울");
        Region savedRegion = regionRepository.save(region);

        //when
        RegionResponseDto byRegionId = regionService.findByRegionId(savedRegion.getId());

        //then
        assertThat(byRegionId.regionName()).isEqualTo(region.getRegionName());
    }

    @DisplayName("지역 수정 테스트")
    @Test
    void update_By_Region() {
        //given
        Region region = new Region("서울");
        Region savedRegion = regionRepository.save(region);
        CreateRegionRequestDto createRegionRequestDto = new CreateRegionRequestDto("부산");

        //when
        RegionResponseDto regionResponseDto = regionService.updateRegion(savedRegion.getId(),
            createRegionRequestDto);

        //then
        assertThat(regionResponseDto.regionName()).isEqualTo(createRegionRequestDto.regionName());
    }

    @DisplayName("지역 삭제 테스트")
    @Test
    void delete_By_Region() {
        //given
        Region region = new Region("서울");
        Region savedRegion = regionRepository.save(region);

        User user = new User("테스터", "1234", UserRoleEnum.MASTER);
        User savedUser = userRepository.save(user);
        //when
        Region deleteBeforeRegion = regionRepository.findById(savedRegion.getId())
            .orElseThrow(() -> new RuntimeException("없는 지역 입니다."));
        regionService.deleteByRegionId(savedRegion.getId(), savedUser.getId());

        // then 삭제 후 조회하면 예외가 발생해야 함
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            regionRepository.findById(savedRegion.getId())
                .orElseThrow(() -> new RuntimeException("삭제된 지역이 조회되지 않음"));
        });

        assertThat(exception.getMessage()).isEqualTo("삭제된 지역이 조회되지 않음");
        //삭제 이전에 정상 조회 되는지 검증
        assertThat(deleteBeforeRegion.getRegionName()).isEqualTo(region.getRegionName());
    }

    @Disabled
    @DisplayName("Region 동적 쿼리 테스트")
    @Test
    @Transactional
    void DynamicSearch_Region() {
        //given

        List<Region> regions1 = IntStream.rangeClosed(1, 10)
            .mapToObj(i -> Region.builder()
                .regionName("서울시")
                .build()
            )
            .toList();

        List<Region> regions2 = IntStream.rangeClosed(1, 5)
            .mapToObj(i -> Region.builder()
                .regionName("부산시")
                .build()
            )
            .toList();

        regionRepository.saveAll(regions1);
        regionRepository.saveAll(regions2);

        //when
        // 검색 조건1
        RegionSearchRequestDto Case1 = new RegionSearchRequestDto(
            "서울",
            0,
            5,
            Collections.singletonList("createdAt")
        );

        // 검색 조건2
        RegionSearchRequestDto Case2 = new RegionSearchRequestDto(
            "부산",
            0,
            5,
            Collections.singletonList("createdAt")
        );

        Page<RegionSearchResponseDto> result1 = regionService.searchRegion(Case1);

        Page<RegionSearchResponseDto> result2 = regionService.searchRegion(Case2);

        //then
        assertThat(result1).isNotEmpty();
        assertThat(result1.getTotalElements()).isEqualTo(10);
        assertThat(result1.getContent().get(0).regionName()).isEqualTo("서울시");

        assertThat(result2).isNotEmpty();
        assertThat(result2.getTotalElements()).isEqualTo(5);
        assertThat(result2.getContent().get(0).regionName()).isEqualTo("부산시");


    }

    private User createMaster(String username) {
        return new User(username, "password", UserRoleEnum.MASTER);
    }

    private Region createRegion(String name) {
        return new Region("서울");
    }

    private Store createStore(String name, String content, String address) {
        return Store.builder()
            .name(name)
            .content(content)
            .address(address)
            .build();
    }


}