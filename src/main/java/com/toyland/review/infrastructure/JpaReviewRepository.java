package com.toyland.review.infrastructure;

import com.toyland.review.model.Review;
import com.toyland.review.model.repository.ReviewRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReviewRepository extends ReviewRepository, JpaRepository<Review, UUID>,
    ReviewRepositoryCustom {

}
