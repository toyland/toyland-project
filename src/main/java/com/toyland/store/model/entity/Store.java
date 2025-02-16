/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.model.entity;

import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "store_id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "store_name", nullable = false, length = 100)
  private String name;

  @Column(name = "content", length = 100)
  private String content;

  @Column(name = "address", length = 100)
  private String address;

  @Builder
  private Store(String address, String content, String name) {
    this.address = address;
    this.content = content;
    this.name = name;
  }

  public static Store from(CreateStoreRequestDto dto) {
    return Store.builder()
        .name(dto.name())
        .content(dto.content())
        .address(dto.address())
        .build();

  }
}
