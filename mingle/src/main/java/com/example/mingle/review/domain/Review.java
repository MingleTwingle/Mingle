package com.example.mingle.review.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private String author = "익명";  // 작성자 (기본값: 익명)

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 작성일시

    private LocalDateTime updatedAt;  // 수정일시

    @Column(nullable = false)
    private int viewCount = 0;  // 조회수 (기본값 0)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewCategory category;  // 숙소, 식당

}