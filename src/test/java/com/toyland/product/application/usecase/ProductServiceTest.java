/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;
import com.toyland.product.application.usecase.dto.UpdateProductServiceRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.product.presentaion.dto.CreateProductRequestDto;
import com.toyland.product.presentaion.dto.ProductResponseDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class ProductServiceTest extends IntegrationTestSupport {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private StoreRepository storeRepository;

  @DisplayName("상품을 생성합니다.")
  @Test
  @Transactional
  void createProduct() {
    // given
    Store goobne = storeRepository.save(createStore("굽네치킨", "굽네치킨입니다.", "경기도 성남시 분당구 가로 1"));

    // when
    productService.createProduct(
        CreateProductServiceRequestDto.of(
            new CreateProductRequestDto("고추바사삭", BigDecimal.valueOf(100000), false, goobne.getId())
            , goobne
        )
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
  void readProduct(){
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

  @DisplayName("상품을 업데이트합니다.")
  @Test
  void test(){
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

  private Product createProduct(String name, Store store) {
    return Product.builder()
        .name(name)
        .price(BigDecimal.valueOf(20000))
        .isDisplay(true)
        .store(store)
        .build();
  }

  private Store createStore(String name, String content, String address){
    return Store.builder()
        .name(name)
        .content(content)
        .address(address)
        .build();
  }
}