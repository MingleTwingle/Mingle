package com.example.mingle.review.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFilterForm {
    private Integer restaurantId;
    private Integer accommodationRoomId;
    private Integer reviewScore;
}
