/**
 * @Date : 2025. 02. 17.
 * @author : jieun(je-pa)
 */
package com.toyland.product.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.product.application.usecase.dto.CreateProductServiceRequestDto;
import com.toyland.product.model.entity.Product;
import com.toyland.product.model.repository.ProductRepository;
import com.toyland.product.presentaion.dto.CreateProductRequestDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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

  @AfterEach
  void tearDown() {
    productRepository.deleteAllInBatch();
    storeRepository.deleteAllInBatch();
  }

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

  private Store createStore(String name, String content, String address){
    return Store.builder()
        .name(name)
        .content(content)
        .address(address)
        .build();
  }
}