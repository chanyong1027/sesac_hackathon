package com.example.festival.dto;

import com.example.festival.domain.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private String userId;
    private String mt20id;
    private int rating;
    private String review;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getId(); // 엔티티의 id 필드
        this.mt20id = review.getMt20id();
        this.userId = review.getUserId();
        this.rating = review.getRating();
        this.review = review.getReview();
        this.createdAt = review.getCreatedAt();
    }
}