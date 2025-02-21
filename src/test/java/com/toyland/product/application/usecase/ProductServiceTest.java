/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.product.application.usecase.dto.DeleteProductServiceRequestDto;
import com.toyland.product.application.usecase.dto.UpdateProductServiceRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.product.presentation.dto.CreateProductRequestDto;
import com.toyland.product.presentation.dto.ProductResponseDto;
import com.toyland.product.presentation.dto.ProductWithStoreResponseDto;
import com.toyland.product.presentation.dto.SearchProductRequestDto;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

class ProductServiceTest extends IntegrationTestSupport {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private RegionRepository regionRepository;

  @Autowired
  private UserRepository userRepository;

  @DisplayName("상품을 생성합니다.")
  @Test
  @Transactional
  void createProduct() {
    // given
    Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));

    // when
    productService.createProduct(
        new CreateProductRequestDto("고추바사삭", BigDecimal.valueOf(100000), false, goobne.getId())
    );

    // then
    List<Product> all = productRepository.findAll();
    assertThat(all).hasSize(1)
        .extracting("name", "price", "isDisplay", "store.name")
        .containsExactlyInAnyOrder(
            tuple("고추바사삭", BigDecimal.valueOf(100000), false, "굽네치킨")
        );

  }

  @DisplayName("상품을 단건 조회합니다.")
  @Test
  void readProduct() {
    // given
    Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));
    Product product1 = productRepository.save(createProduct("고추바사삭", goobne));
    Product product2 = productRepository.save(createProduct("고추바사삭", goobne));

    // when
    ProductResponseDto result = productService.readProduct(product1.getId());

    // then
    assertThat(result)
        .extracting("name", "id")
        .contains(product1.getName(), product1.getId());

  }

  @DisplayName("상품을 검색합니다.")
  @Test
  void searchProducts() {
    // given
    User owner = userRepository.save(createOwner("오너"));
    Region region = regionRepository.save(createRegion("경기도"));
    Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1", owner, region));
    List<Product> products = new ArrayList<>();
    for(int i=0 ; i<25 ; i++){
      products.add(createProduct(String.format("고추바사삭 %03d", i), goobne,i%2 == 0));
    }
    for(int i=0 ; i<25 ; i++){
      products.add(createProduct(String.format("치킨 %03d", i, i%2 == 0), goobne,i%2 == 1));
    }
    productRepository.saveAll(products);

    // when
    Page<ProductWithStoreResponseDto> result = productService.searchProducts(
        SearchProductRequestDto.builder()
            .searchText("고추바사삭")
            .storeId(goobne.getId())
            .isDisplay(true)
            .page(2)
            .size(10)
            .sort(List.of("name", "asc"))
            .build()
    );

    // then
    List<Product> expected = List.of(products.get(20), products.get(22), products.get(24));

    assertThat(result.getContent()).hasSize(3)
        .extracting("productId", "store.storeId", "store.ownerName")
        .containsExactlyInAnyOrder(
            expected.stream().map(
                (ex) -> tuple(ex.getId(), ex.getStore().getId(), ex.getStore().getOwner().getUsername())
            ).toArray(Tuple[]::new)
        );
  }

  private Region createRegion(String name) {
    return Region.builder()
        .regionName(name)
        .build();
  }

  @DisplayName("상품을 업데이트합니다.")
  @Test
  void updateProduct() {
    // given
    Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));
    Product product1 = productRepository.save(createProduct("고추바사삭", goobne));
    String newName = "new 이름";
    BigDecimal newPrice = BigDecimal.valueOf(29000);
    boolean newIsDisplay = true;

    // when
    ProductResponseDto result = productService.updateProduct(
        UpdateProductServiceRequestDto.builder()
            .id(product1.getId())
            .name(newName)
            .price(newPrice)
            .isDisplay(newIsDisplay)
            .build());

    // then
    assertThat(result)
        .extracting("name", "id", "price", "isDisplay")
        .contains(newName, product1.getId(), newPrice, newIsDisplay);
  }

  @DisplayName("상품을 삭제합니다.")
  @Test
  void deleteProduct() {
    // given
    Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));
    Product product1 = productRepository.save(createProduct("고추바사삭", goobne));
    User user = userRepository.save(createOwner("유저1"));
    LocalDateTime now = LocalDateTime.now();

    // when
    productService.deleteProduct(
        DeleteProductServiceRequestDto.builder()
            .actorId(user.getId())
            .productId(product1.getId())
            .eventDateTime(now)
            .build()
    );

    // then
    List<Product> result = productRepository.findAll();
    assertThat(result).hasSize(0);
  }

  private User createOwner(String username) {
    return new User(username, "password", UserRoleEnum.OWNER);
  }

  private Product createProduct(String name, Store store, boolean isDisplay){
    return Product.builder()
        .name(name)
        .price(BigDecimal.valueOf(20000))
        .isDisplay(isDisplay)
        .store(store)
        .build();
  }

  private Product createProduct(String name, Store store) {
    return this.createProduct(name, store, true);
  }

  private Store createStore(String name, String content, String address, User owner, Region region) {
    return Store.builder()
        .name(name)
        .content(content)
        .address(address)
        .owner(owner)
        .region(region)
        .build();
  }

  private Store createStore(String name, String content, String address) {
    return createStore(name, content, address, null, null);
  }
}