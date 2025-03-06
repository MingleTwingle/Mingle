package com.example.mingle.review.service;

import com.example.mingle.review.domain.Review;
import com.example.mingle.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // ✅ 리뷰 등록
    public Integer addReview(Review review) {
        reviewRepository.save(review);
        return review.getReviewId();
    }

    // ✅ 리뷰 수정
    public Review updateReview(Integer review_id, Review updatedReview) {
        return reviewRepository.findById(review_id)
                .map(existingReview -> {
                    existingReview.setReviewComment(updatedReview.getReviewComment());
                    existingReview.setReviewScore(updatedReview.getReviewScore());
                    return reviewRepository.save(existingReview);
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. ID: " + review_id));
    }

    // ✅ 리뷰 삭제
    public void deleteReview(Integer review_id) {
        reviewRepository.deleteById(review_id);
    }

    // ✅ 특정 리뷰 조회 (ID로 조회)
    public Optional<Review> getReviewById(Integer review_id) {
        return reviewRepository.findById(review_id);
    }

    // ✅ 모든 리뷰 조회
    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }

    // ✅ 필터링 검색 (식당 ID / 숙박 시설 ID / 최소 평점)
    public List<Review> searchReviews(Integer restaurantId, Integer accommodationRoomId, Integer minScore) {
        return reviewRepository.findAll().stream()
                .filter(review -> restaurantId == null || (review.getRestaurantId() != null && review.getRestaurantId().equals(restaurantId)))
                .filter(review -> accommodationRoomId == null || (review.getAccommodationRoomId() != null && review.getAccommodationRoomId().equals(accommodationRoomId)))
                .filter(review -> minScore == null || review.getReviewScore() >= minScore)
                .collect(Collectors.toList());
    }
}