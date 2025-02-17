/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StoreServiceTest extends IntegrationTestSupport {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        storeRepository.deleteAllInBatch();
        regionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("상점을 저장한다.")
    @Test
    void createStore() {
        // given
        Region region = regionRepository.save(createRegion("서울"));
        User owner = userRepository.save(createMaster("홍길동"));

        // when
        storeService.createStore(new CreateStoreRequestDto(
            "굽네치킨",
            "굽네치킨입니다.",
            "경기도 성남시 분당구 가로 1",
            region.getId(),
            owner.getId()));

        // then
        List<Store> all = storeRepository.findAll();
        assertThat(all).hasSize(1)
            .extracting("name", "content", "address")
            .containsExactlyInAnyOrder(
                tuple("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1")
            );
    }

    @DisplayName("상품을 조회합니다.")
    @Test
    void readStore() {
        // given
        Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));
        Store nene = storeRepository.save(createStore("네네치킨", "네네치킨입니다.", "경기도 성남시 분당구 가로 2"));

        // when
        Store result = storeService.readStore(goobne.getId());

        // then
        assertThat(result)
            .extracting("name", "content", "address")
            .contains("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1");
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