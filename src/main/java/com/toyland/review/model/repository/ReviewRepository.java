package com.toyland.review.model.repository;

import com.toyland.review.model.Review;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository {

  Review save(Review review);

  Optional<Review> findById(UUID reviewId);

  Page<Review> searchReviews(UUID storeId, Pageable pageable);

  Optional<List<Review>> getReviewList(UUID uuid);

  List<Review> findByStoreId(UUID storeId);

  @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.store.id = :storeId")
  Double updateStoreRating(UUID storeId);
}
