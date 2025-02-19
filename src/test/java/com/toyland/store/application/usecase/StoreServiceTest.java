/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.toyland.category.model.entity.Category;
import com.toyland.category.model.repository.CategoryRepository;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.store.application.usecase.dto.CreateStoreCategoryListServiceRequestDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.storecategory.model.entity.StoreCategory;
import com.toyland.storecategory.model.repository.StoreCategoryRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class StoreServiceTest extends IntegrationTestSupport {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        storeCategoryRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        regionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("음식점을 저장한다.")
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

    @DisplayName("음식점을 조회합니다.")
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

    @DisplayName("상점에 카테고리들을 설정 - 기존 상점의 카테고리를 제거 후 새로운 카테고리를 입력합니다.")
    @Transactional
    @Test
    void setStoreCategories(){
        // given
        User user = userRepository.save(createMaster("master1"));

        Store goobne = storeRepository.save(createStore("굽네치킨"));

        Category rootCategory = categoryRepository.save(createCategory("가게 배달", null));
        Category chickenCategory = categoryRepository.save(createCategory("치킨", rootCategory));
        Category lateNightSnackCategory = categoryRepository.save(createCategory("야식", rootCategory));
        List<UUID> categoryIdList = List.of(chickenCategory.getId(), lateNightSnackCategory.getId());

        LocalDateTime now = LocalDateTime.now();

        // when
        storeService.setStoreCategories(CreateStoreCategoryListServiceRequestDto
            .builder()
                .categoryIdList(categoryIdList)
                .storeId(goobne.getId())
                .actorId(user.getId())
                .eventTime(now)
            .build());

        // then
        List<StoreCategory> result = storeCategoryRepository.findAll();
        assertThat(result).hasSize(2)
            .extracting("store", "category")
            .containsExactlyInAnyOrder(
                tuple(goobne, chickenCategory),
                tuple(goobne, lateNightSnackCategory)
            );
    }

    private Category createCategory(String name, Category parentCategory) {
        return Category.builder().name(name).parent(parentCategory).build();
    }

    private User createMaster(String username) {
        return new User(username, "password", UserRoleEnum.MASTER);
    }

    private Region createRegion(String name) {
        return new Region("서울");
    }

    private Store createStore(String name){
        return this.createStore(name, "설명", "주소");
    }

    private Store createStore(String name, String content, String address) {
        return Store.builder()
            .name(name)
            .content(content)
            .address(address)
            .build();
    }
}