package com.toyland.review.presentation;

import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotation;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotationList;
import com.toyland.global.config.swagger.response.CustomApiResponse;
import com.toyland.global.config.swagger.response.HttpSuccessCode;
import com.toyland.global.exception.type.ApiErrorCode;
import com.toyland.review.application.facade.ReviewFacade;
import com.toyland.review.presentation.dto.ReviewRequestDto;
import com.toyland.review.presentation.dto.ReviewResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "리뷰", description = "Review API")
public class ReviewController {

    private final ReviewFacade reviewFacade;

    /**
     * 리뷰를 생성한다.
     *
     * @param review
     * @return 생성한 review
     */
    @Operation(summary = "review 등록", description = "review 등록 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "review 등록 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PostMapping
    public ResponseEntity<CustomApiResponse<ReviewResponseDto>> createReview(
        @Valid @RequestBody ReviewRequestDto review) {
        ReviewResponseDto reviewResponseDto = reviewFacade.createReview(review);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/reviews/{reviewsId}")
            .buildAndExpand(reviewResponseDto.getReviewId())
            .toUri();

        return ResponseEntity
            .created(uri)
            .body(CustomApiResponse.of(HttpSuccessCode.REVIEW_CREATE, reviewResponseDto));
    }

    /**
     * 리뷰 한 건을 조회한다.
     *
     * @param reviewId
     * @return 리뷰 내용과 점수
     */
    @Operation(summary = "review 조회", description = "review 조회 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "review 조회 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/{reviewId}")
    public ResponseEntity<CustomApiResponse<ReviewResponseDto>> getReview(
        @PathVariable UUID reviewId) {
        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.REVIEW_FIND_ONE,
                reviewFacade.getReview(reviewId)));
    }

    /**
     * 특정 음식점의 전체 리뷰를 조회한다.
     *
     * @param storeId 음식점Id
     * @return 리뷰리스트와 점수
     */
    @Operation(summary = "review 검색", description = "review 검색 api 입니다." +
        " 예시 http://localhost:8080/api/v1/reviews/search?page=0&size=10&sort=createdAt,asc&\n"
        + "    storeId=2e11ee82-82a9-41cc-ac52-3b532887d5e6")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "review 검색 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/search")
    public ResponseEntity<CustomApiResponse<Page<ReviewResponseDto>>> searchReview(
        Pageable pageable,
        @RequestParam UUID storeId) {
        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.REVIEW_SEARCH,
                reviewFacade.searchReview(pageable, storeId)));

    }

    /**
     * 리뷰의 내용과 점수를 수정한다.
     *
     * @param reviewId
     * @param review   내용과 점수
     * @return 수정된 리뷰
     */
    @Operation(summary = "review 수정", description = "review 수정 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "review 수정 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PutMapping("/{reviewId}")
    public ResponseEntity<CustomApiResponse<ReviewResponseDto>> updateReview(
        @PathVariable UUID reviewId,
        @Valid @RequestBody ReviewRequestDto review) {
        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.QNA_UPDATE,
            reviewFacade.updateReview(review, reviewId)));

    }

    /**
     * 리뷰 한 건 삭제한다.
     *
     * @param reviewId
     * @param loginUserId
     */
    @Operation(summary = "review 삭제", description = "review 삭제 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "review 삭제 성공"),
    })
    @ApiErrorCodeAnnotationList({ApiErrorCode.INVALID_REQUEST, ApiErrorCode.UNAUTHORIZED})
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CustomApiResponse<Void>> deleteReview(@PathVariable UUID reviewId,
        @CurrentLoginUserId Long loginUserId) {
        reviewFacade.deleteReview(reviewId, loginUserId);

        return ResponseEntity.noContent().build();
    }


}
