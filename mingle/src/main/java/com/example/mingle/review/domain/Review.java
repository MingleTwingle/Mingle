package com.example.mingle.review.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 게시글 id

    @Column(nullable = false)
    private String title;  // 제목

    @Column(nullable = false, length = 2000)
    private String content;  // 내용

    @Column(nullable = false)
    @Builder.Default
    private String author = "익명";  // 작성자 (기본값: 익명)

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();  // 작성일시

    private LocalDateTime updatedAt;  // 수정일시

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;  // 조회수 (기본값 0)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewCategory category;  // 숙소, 식당

}