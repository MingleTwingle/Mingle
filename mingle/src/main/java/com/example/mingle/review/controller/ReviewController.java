package com.example.mingle.review.controller;

import com.example.mingle.review.domain.Review;
import com.example.mingle.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ✅ 리뷰 등록 폼
    @GetMapping("/reviews/new")
    public String createForm(Model model) {
        model.addAttribute("reviewForm", new ReviewForm());
        return "review/reviewRegister";
    }

    // ✅ 리뷰 등록
    @PostMapping("/reviews/new")
    public String create(@Validated ReviewForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "review/reviewRegister";
        }
        Review review = new Review();
        review.setReviewComment(form.getReviewComment());
        review.setReviewScore(form.getReviewScore());
        review.setRestaurantId(form.getRestaurantId());
        review.setAccommodationRoomId(form.getAccommodationRoomId());
        review.setGuestKey(form.getGuestKey());

        reviewService.addReview(review);
        return "redirect:/reviews/list";
    }

    // ✅ 리뷰 목록 조회
    @GetMapping("/reviews/list")
    public String list(Model model) {
        List<Review> reviews = reviewService.findAllReviews();
        model.addAttribute("reviews", reviews);
        return "review/reviewList";
    }

    // ✅ 리뷰 필터링 폼 및 필터링된 리뷰 목록 조회
    @GetMapping("/reviews/filter")
    public String filterList(Model model) {
        model.addAttribute("reviewFilterForm", new ReviewFilterForm());
        List<Review> reviews = reviewService.findAllReviews();  // 초기에는 전체 리뷰를 가져옴
        model.addAttribute("reviews", reviews);
        return "review/reviewFilterAndList";  // 새로운 HTML 파일을 반환
    }

    // ✅ 리뷰 필터링 및 필터링된 리뷰 목록 보기
    @PostMapping("/reviews/filter")
    public String customFilter(@Validated ReviewFilterForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "review/reviewFilterAndList";  // 오류가 있으면 다시 필터링 페이지로 돌아감
        }
        List<Review> filteredReviews = reviewService.searchReviews(
                form.getRestaurantId(),
                form.getAccommodationRoomId(),
                form.getReviewScore()
        );
        model.addAttribute("reviews", filteredReviews);
        return "review/reviewFilterAndList";  // 필터링된 리뷰를 보여주는 페이지 반환
    }
}
