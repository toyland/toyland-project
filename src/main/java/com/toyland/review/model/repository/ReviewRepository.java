package com.toyland.review.model.repository;

import com.toyland.review.model.Review;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepository {

  Review save(Review review);

  Optional<Review> findById(UUID reviewId);

  Page<Review> searchReviews(UUID storeId, Pageable pageable);
}
