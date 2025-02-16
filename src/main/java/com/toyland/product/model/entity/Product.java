package com.toyland.product.model.entity;

import com.toyland.product.presentaion.dto.CreateProductRequestDto;
import com.toyland.store.model.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "product_id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "product_name", nullable = false, length = 100)
  private String name;

  @Column(name = "product_price", nullable = false)
  private BigDecimal price;

  @Column(name = "store_name", nullable = false)
  private boolean isDisplay;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @Builder
  private Product(boolean isDisplay, String name, BigDecimal price, Store store) {
    this.isDisplay = isDisplay;
    this.name = name;
    this.price = price;
    this.store = store;
  }

  public static Product of(CreateProductRequestDto dto, Store store) {
    return Product.builder()
        .name(dto.name())
        .price(dto.price())
        .isDisplay(dto.isDisplay())
        .store(store)
        .build();
  }
}
