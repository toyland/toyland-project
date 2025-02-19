package com.toyland.review.application.usecase;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import com.toyland.order.model.Order;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.review.model.Review;
import com.toyland.review.model.repository.ReviewRepository;
import com.toyland.review.presentation.dto.ReviewRequestDto;
import com.toyland.review.presentation.dto.ReviewResponseDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final StoreRepository storeRepository;
  private final OrderRepository orderRepository;

  @Override
  @Transactional
  public ReviewResponseDto createReview(ReviewRequestDto reviewDto) {
    UUID storeId = UUID.fromString(reviewDto.getStoreId());
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));

    UUID orderId = UUID.fromString(reviewDto.getOrderId());
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));

    Review review = Review.from(reviewDto, store, order);
    Review result = reviewRepository.save(review);
    return ReviewResponseDto.of(result);
  }

  @Override
  public ReviewResponseDto getReivew(UUID reviewId) {
    Review review = reviewRepository.findById(reviewId).orElseThrow(
        () -> CustomException.from(ProductErrorCode.NOT_FOUND)
    );
    return ReviewResponseDto.of(review);
  }

  @Override
  public Page<ReviewResponseDto> searchReview(Pageable pageable, UUID storeId) {
    Page<Review> reviewPage = reviewRepository.searchReviews(storeId, pageable);
    Page<ReviewResponseDto> reviewList = reviewPage.map(ReviewResponseDto::of);
    return reviewList;
  }


  @Override
  @Transactional
  public ReviewResponseDto updateReview(ReviewRequestDto reviewDto, UUID reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));
    review.update(reviewDto);
    return ReviewResponseDto.of(review);
  }

  @Override
  @Transactional
  public void deleteReview(UUID reviewId, Long id) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));
    review.addDeletedField(id);
  }


}
