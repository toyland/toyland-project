package com.toyland.review.model;

import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.order.model.Order;
import com.toyland.review.presentation.dto.ReviewRequestDto;
import com.toyland.store.model.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("deleted_at IS NULL")
@Entity
@Getter
@Table(name = "p_review")
@NoArgsConstructor
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID reviewId;

  @Column
  private String reviewContent;

  @Column
  private Integer rating;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false, unique = true)
  private Order order;

  @Builder
  public Review(String reviewContent, Integer rating, Store store, Order order) {
    this.reviewContent = reviewContent;
    this.rating = rating;
    this.store = store;
    this.order = order;
  }

  public static Review from(ReviewRequestDto reviewRequestDto, Store store, Order order) {
    return new Review(
        reviewRequestDto.getReviewContent(),
        reviewRequestDto.getRating(),
        store,
        order
    );
  }


  public void update(ReviewRequestDto reviewDto) {
    this.reviewContent = reviewDto.getReviewContent();
    this.rating = reviewDto.getRating();
  }
}
