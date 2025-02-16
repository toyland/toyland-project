package com.toyland.region.application.usecase;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.region.presentation.dto.CreateRegionRequestDto;
import com.toyland.region.presentation.dto.RegionResponseDto;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    UserRepository userRepository;


    @AfterEach
    void tearDown() {
        regionRepository.deleteAll();
        userRepository.deleteAll();
    }

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
        RegionResponseDto regionResponseDto = regionService.updateRegion(savedRegion.getId(), createRegionRequestDto);

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

}