package com.toyland.review.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

  @NotNull(message = "Order ID is required")
  private String orderId;

  @NotNull(message = "Store ID is required")
  private String storeId;

  @NotBlank(message = "Review content cannot be empty")
  private String reviewContent;

  @NotNull(message = "Rating is required")
  @Min(value = 1, message = "Rating must be at least 1")
  @Max(value = 5, message = "Rating cannot be more than 5")
  private Integer rating;

  public ReviewRequestDto(String orderId, String storeId, String reviewContent, Integer rating) {
    this.orderId = orderId;
    this.storeId = storeId;
    this.reviewContent = reviewContent;
    this.rating = rating;
  }

}
