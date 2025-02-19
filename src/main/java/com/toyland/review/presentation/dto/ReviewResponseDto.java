package com.toyland.review.presentation.dto;

import com.toyland.review.model.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {

  private String reviewContent;
  private Integer rating;

  public ReviewResponseDto(String reviewContent, Integer rating) {
    this.reviewContent = reviewContent;
    this.rating = rating;
  }


  public static ReviewResponseDto of(Review review) {
    return new ReviewResponseDto(
        review.getReviewContent(),
        review.getRating()
    );
  }
}
