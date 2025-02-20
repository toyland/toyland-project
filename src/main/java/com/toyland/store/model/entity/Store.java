/**
 * @Date : 2025. 02. 16.
 * @author : jieun(je-pa)
 */
package com.toyland.store.model.entity;

import com.toyland.region.model.entity.Region;
import com.toyland.review.model.Review;
import com.toyland.store.presentation.dto.CreateStoreRequestDto;
import com.toyland.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "region_id")
  private Region region;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private User owner;

  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews = new ArrayList<>();

  public void addReview(Review review) {
    if (this.reviews == null) {
      this.reviews = new ArrayList<>();
    }
    this.reviews.add(review);
    review.setStore(this);
  }

  @Builder
  private Store(String address, String content, String name, Region region, User owner,
      List<Review> reviews) {
    this.address = address;
    this.content = content;
    this.name = name;
    this.region = region;
    this.owner = owner;
    this.reviews = reviews;
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
}
