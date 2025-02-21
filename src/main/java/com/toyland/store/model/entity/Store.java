/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.model.entity;

import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.region.model.entity.Region;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_store")
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "region_id")
  private Region region;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private User owner;
  
  private Double avgRating;

  @Builder
  private Store(String address, String content, String name, Region region, User owner) {
    this.address = address;
    this.content = content;
    this.name = name;
    this.region = region;
    this.owner = owner;
  }


  public static Store of(CreateStoreRequestDto dto, Region region, User owner) {
    return Store.builder()
        .name(dto.name())
        .content(dto.content())
        .address(dto.address())
        .region(region)
        .owner(owner)
        .build();

  }

  public void updateStore(String name, String content, String address, User owner, Region region) {
    this.name = name;
    this.content = content;
    this.address = address;
    this.owner = owner;
    this.region = region;
  }

  public void updateRating(double newAverage) {
    this.avgRating = newAverage;
  }
}
