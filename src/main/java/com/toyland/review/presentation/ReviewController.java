package com.toyland.review.presentation;

import com.toyland.ai.presentation.dto.PagedResponse;
import com.toyland.review.application.facade.ReviewFacade;
import com.toyland.review.presentation.dto.ReviewRequestDto;
import com.toyland.review.presentation.dto.ReviewResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewFacade reviewFacade;

  /**
   * 리뷰를 생성한다.
   *
   * @param review
   * @return 생성한 review
   */
  @PostMapping
  public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto review) {
    return ResponseEntity.ok(reviewFacade.createReview(review));
  }

  /**
   * 리뷰 한 건을 조회한다.
   *
   * @param reviewId
   * @return 리뷰 내용과 점수
   */
  @GetMapping("/{reviewId}")
  public ResponseEntity<ReviewResponseDto> getReview(@PathVariable UUID reviewId) {
    return ResponseEntity.ok(reviewFacade.getReview(reviewId));
  }

  /**
   * 특정 음식점의 전체 리뷰를 조회한다.
   *
   * @param storeId 음식점Id
   * @return 리뷰리스트와 점수
   */
  @GetMapping("/search")
  public ResponseEntity<PagedResponse<ReviewResponseDto>> searchReview(Pageable pageable,
      @RequestParam UUID storeId) {
    Page<ReviewResponseDto> reviews = reviewFacade.searchReview(pageable, storeId);
    return ResponseEntity.ok(new PagedResponse<>(reviews));
  }


}
