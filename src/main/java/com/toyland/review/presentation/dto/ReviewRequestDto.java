package com.toyland.review.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {


  private String orderId;

  private String storeId;

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
