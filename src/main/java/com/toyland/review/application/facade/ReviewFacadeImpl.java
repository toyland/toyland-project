package com.toyland.review.application.facade;

import com.toyland.review.application.usecase.ReviewService;
import com.toyland.review.presentation.dto.ReviewRequestDto;
import com.toyland.review.presentation.dto.ReviewResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReviewFacadeImpl implements ReviewFacade {

  private final ReviewService reviewService;

  @Override
  public ReviewResponseDto createReview(ReviewRequestDto request) {
    return reviewService.createReview(request);
  }

  @Override
  public ReviewResponseDto getReview(UUID reviewId) {
    return reviewService.getReivew(reviewId);
  }

  @Override
  public Page<ReviewResponseDto> searchReview(Pageable pageable, UUID storeId) {
    return reviewService.searchReview(pageable, storeId);
  }

  @Override
  public ReviewResponseDto updateReview(ReviewRequestDto review, UUID reviewId) {
    return reviewService.updateReview(review, reviewId);
  }

  @Override
  public void deleteReview(UUID reviewId, Long id) {
    reviewService.deleteReview(reviewId, id);
  }

}

