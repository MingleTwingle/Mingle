package com.example.mingle.review.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {
    private String reviewComment;
    private Integer reviewScore;
    private Integer restaurantId;
    private Integer accommodationRoomId;
    private Integer guestKey;
}
