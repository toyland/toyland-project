package com.toyland.review.application.facade;

import com.toyland.review.presentation.dto.ReviewRequestDto;
import com.toyland.review.presentation.dto.ReviewResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewFacade {

  ReviewResponseDto createReview(ReviewRequestDto review);

  ReviewResponseDto getReview(UUID reviewId);

  Page<ReviewResponseDto> searchReview(Pageable pageable, UUID storeId);

  ReviewResponseDto updateReview(ReviewRequestDto review, UUID reviewId);

  void deleteReview(UUID reviewId, Long id);

  Double getAvgRate(String storeId);
}
