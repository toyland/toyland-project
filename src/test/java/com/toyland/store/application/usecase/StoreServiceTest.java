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
import com.toyland.store.application.usecase.dto.DeleteStoreServiceRequestDto;
import com.toyland.store.application.usecase.dto.UpdateStoreServiceRequestDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.store.presentation.dto.SearchStoreRequestDto;
import com.toyland.store.presentation.dto.StoreResponseDto;
import com.toyland.store.presentation.dto.StoreWithOwnerResponseDto;
import com.toyland.storecategory.model.entity.StoreCategory;
import com.toyland.storecategory.model.repository.StoreCategoryRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    StoreResponseDto result = storeService.readStore(goobne.getId());

    // then
    assertThat(result)
        .extracting("name", "content", "address")
        .contains("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1");
  }

  @DisplayName("음식점을 검색합니다.")
  @Test
  void searchStores() {
    // given
    Region seoul = createRegion("서울");
    Region busan = createRegion("부산");
    User owner1 = createOwner("owner1");
    User owner2 = createOwner("owner2");

    regionRepository.saveAll(List.of(seoul, busan));
    userRepository.saveAll(List.of(owner1, owner2));
    Category parentCategory = categoryRepository.save(createCategory("가게배달", null));
    Category pizzaCategory = categoryRepository.save(createCategory("피자", parentCategory));
    List<Store> stores = new ArrayList<>();
    List<StoreCategory> storeCategories = new ArrayList<>();

    for(int i=1 ; i<= 13 ; i++){
      stores.add(createStore(String.format("굽네 %03d", i), "", "", owner1, seoul));
      stores.add(createStore(String.format("네네 %03d", i), "", "", owner1, busan));
      stores.add(createStore(String.format("도미노 %03d", i), "", "", owner2, seoul));
      Store busanDomino = createStore(String.format("도미노 %03d", i), "", "", owner2, busan);
      stores.add(busanDomino);

      if(i<=12) storeCategories.add(createStoreCategory(busanDomino, pizzaCategory));
    }

    storeRepository.saveAll(stores);
    storeCategoryRepository.saveAll(storeCategories);

    // when
    Page<StoreWithOwnerResponseDto> result = storeService.searchStores(
        SearchStoreRequestDto.builder()
            .searchText("도미노")
            .categoryNameSearchText("피자")
            .storeNameSearchText("도미노")
            .ownerId(owner2.getId())
            .regionId(busan.getId())
            .categoryId(pizzaCategory.getId())
            .page(2)
            .size(10)
            .sort(List.of("name", "desc"))
            .build()
    );

    // then
    assertThat(result.getTotalElements()).isEqualTo(12);
    assertThat(result).hasSize(2)
        .extracting("storeName", "region.regionId", "owner.ownerId", "category.categoryId")
        .containsExactly(
            tuple("도미노 002", busan.getId(), owner2.getId(), pizzaCategory.getId()),
            tuple("도미노 001", busan.getId(), owner2.getId(), pizzaCategory.getId())
        );
  }

  @DisplayName("음식점을 수정합니다.")
  @Test
  void updateStore() {
    // given
    Region beforeRegion = regionRepository.save(createRegion("서울"));
    User beforeOwner = userRepository.save(createMaster("홍길동"));
    Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));

    Region afterRegion = regionRepository.save(createRegion("부산"));
    User afterOwner = userRepository.save(createMaster("유재석"));
    String newName = "굽네";

    // when
    StoreResponseDto result = storeService.updateStore(
        UpdateStoreServiceRequestDto.builder()
            .id(goobne.getId())
            .regionId(afterRegion.getId())
            .ownerId(afterOwner.getId())
            .name(newName)
            .content(goobne.getContent())
            .address(goobne.getAddress())
            .build()
    );

    // then
    assertThat(result)
        .extracting("name", "regionName", "ownerName")
        .contains(newName, afterRegion.getRegionName(), afterOwner.getUsername());

  }

  @DisplayName("음식점을 삭제합니다.")
  @Test
  void deleteStore() {
    // given
    Store go = createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1");
    Store ne = createStore("네네치킨", "네네치킨입니다.", "경기도 성남시 분당구 가로 2");
    storeRepository.saveAll(List.of(go, ne));
    User actor = userRepository.save(createMaster("유재석"));

    // when
    storeService.deleteStore(
        DeleteStoreServiceRequestDto.of(actor.getId(), go.getId(), LocalDateTime.now())
    );

    // then
    List<Store> result = storeRepository.findAll();
    assertThat(result).hasSize(1)
        .extracting("id", "name")
        .containsExactlyInAnyOrder(
            tuple(ne.getId(), ne.getName())
        );
  }


  @DisplayName("상점에 카테고리들을 설정 - 기존 상점의 카테고리를 제거 후 새로운 카테고리를 입력합니다.")
  @Transactional
  @Test
  void setStoreCategories() {
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

  private StoreCategory createStoreCategory(Store store, Category category){
    return new StoreCategory(store, category);
  }

  private Category createCategory(String name, Category parentCategory) {
    return Category.builder().name(name).parent(parentCategory).build();
  }

  private User createOwner(String username) {
    return new User(username, "password", UserRoleEnum.OWNER);
  }

  private User createMaster(String username) {
    return new User(username, "password", UserRoleEnum.MASTER);
  }

  private Region createRegion(String name) {
    return new Region("서울");
  }

  private Store createStore(String name) {
    return this.createStore(name, "설명", "주소");
  }

  private Store createStore(String name, String content, String address) {
    return createStore(name, content, address, null, null);
  }

  private Store createStore(String name, String content, String address, User owner,
      Region region) {
    return Store.builder()
        .name(name)
        .content(content)
        .address(address)
        .owner(owner)
        .region(region)
        .build();
  }
}