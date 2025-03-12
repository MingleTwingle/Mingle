package com.example.mingle.review.repository;

import com.example.mingle.review.domain.Review;
import com.example.mingle.review.domain.ReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // ✅ 특정 카테고리의 리뷰 조회
    List<Review> findByCategory(ReviewCategory category);

    // ✅ 제목에서 특정 키워드를 포함하는 리뷰 조회
    List<Review> findByTitleContaining(String keyword);
}
