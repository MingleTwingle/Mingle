package com.example.mingle.review.controller;

import com.example.mingle.review.domain.Review;
import com.example.mingle.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // ✅ 리뷰 목록 조회 (검색 & 카테고리 필터 가능)
    @GetMapping
    public String getReviews(@RequestParam(required = false) String category,
                             @RequestParam(required = false) String keyword,
                             Model model) {
        List<Review> reviews = reviewService.getReviews(category, keyword);
        model.addAttribute("reviews", reviews);
        return "review/reviewList"; // ✅ 리뷰 목록 페이지 (`reviewList.html`)
    }

    // ✅ 리뷰 작성 페이지 (폼)
    @GetMapping("/new")
    public String createReviewForm(Model model) {
        model.addAttribute("review", new Review());
        return "review/reviewRegister"; // ✅ 리뷰 작성 페이지 (`reviewRegister.html`)
    }


    // ✅ 리뷰 저장 (AJAX 요청 처리)
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveReview(@RequestBody Review review) {
        Map<String, Object> response = new HashMap<>();
        reviewService.saveReview(review);
        response.put("success", true);
        response.put("message", "리뷰 등록 성공");
        return ResponseEntity.ok(response);
    }

    // ✅ 리뷰 상세 조회
    @GetMapping("/{id}")
    public String getReview(@PathVariable Long id, Model model) {
        Review review = reviewService.getReview(id);
        if (review == null) {
            return "redirect:/reviews"; // ✅ 없는 리뷰 ID일 경우 목록으로 이동
        }
        model.addAttribute("review", review);
        return "review/reviewDetail"; // ✅ 리뷰 상세 페이지 (`reviewDetail.html`)
    }

    // ✅ 리뷰 삭제
    @PostMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews"; // ✅ 삭제 후 목록으로 이동
    }
    
}
