package com.example.mingle.review.repository;

import com.example.mingle.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Optional<Review> findById(Integer reviewId);

    List<Review> findByRestaurantId(Integer restaurantId);

    List<Review> findByAccommodationRoomId(Integer accommodationRoomId);

    List<Review> findByGuestKey(Integer guestKey);

    @Query("SELECT r FROM Review r " +
            "WHERE (:restaurant_id IS NULL OR r.restaurantId = :restaurant_id) " +
            "AND (:accommodationRoom_Id IS NULL OR r.accommodationRoomId = :accommodationRoom_Id) " +
            "AND (:guest_key IS NULL OR r.guestKey = :guest_key) " +
            "AND (:review_score IS NULL OR r.reviewScore >= :review_score)")
    List<Review> findByFilters(
            @Param("restaurant_id") Integer restaurantId,
            @Param("accommodationRoom_Id") Integer accommodationRoomId,
            @Param("guest_key") Integer guestKey,
            @Param("review_score") Integer reviewScore);
}
