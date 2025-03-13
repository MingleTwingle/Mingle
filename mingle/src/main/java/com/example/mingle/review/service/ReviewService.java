package com.example.mingle.review.service;

import com.example.mingle.review.domain.Review;
import com.example.mingle.review.domain.ReviewCategory;
import com.example.mingle.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // ✅ 1. 리뷰 목록 조회 (카테고리 또는 키워드 검색 가능)
    public List<Review> getReviews(String category, String keyword) {
        if (category != null && !category.isEmpty()) {
            return reviewRepository.findByCategory(ReviewCategory.valueOf(category));
        }
        if (keyword != null && !keyword.isEmpty()) {
            return reviewRepository.findByTitleContaining(keyword);
        }
        return reviewRepository.findAll();
    }

    // ✅ 2. 리뷰 저장
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    // ✅ 3. 리뷰 상세 조회
    public Review getReview(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.orElse(null);
    }
    // ✅ 리뷰 삭제 (메서드 추가)
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

}
