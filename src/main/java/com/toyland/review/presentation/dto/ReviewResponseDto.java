package com.toyland.review.presentation.dto;

import com.toyland.review.model.Review;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {

    private UUID reviewId;
    private String reviewContent;
    private Integer rating;

    public ReviewResponseDto(UUID reviewId, String reviewContent, Integer rating) {
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
        this.rating = rating;
    }


    public static ReviewResponseDto of(Review review) {
        return new ReviewResponseDto(
            review.getReviewId(),
            review.getReviewContent(),
            review.getRating()
        );
    }
}
