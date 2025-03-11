package com.example.mingle.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_review")

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;
    @Column(name = "review_comment",nullable = false)
    private String reviewComment;
    @Column(name = "review_score",nullable = false)
    private Integer reviewScore;
    @Column(name = "restaurant_id",nullable = true)
    private Integer restaurantId;
    @Column(name = "accommodation_room_Id",nullable = true)
    private Integer accommodationRoomId;
    @Column(name = "guest_key",nullable = false)
    private Integer guestKey;

}