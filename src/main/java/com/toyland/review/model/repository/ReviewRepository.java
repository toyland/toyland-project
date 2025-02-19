package com.toyland.review.model.repository;

import com.toyland.review.model.Review;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {

  Review save(Review review);

  Optional<Review> findById(UUID reviewId);
}
