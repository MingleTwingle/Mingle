package com.example.mingle.review.repository;

import com.example.mingle.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaReviewRepository extends JpaRepository<Review, Integer>, ReviewRepository {

    @Override
    List<Review> findByRestaurantId(Integer restaurantId);
}
