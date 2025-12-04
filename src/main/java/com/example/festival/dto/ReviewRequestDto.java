package com.example.festival.dto;

import com.example.festival.domain.Review;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewRequestDto {
    @NotNull
    private String userId;

    @NotNull
    private String review;

    @NotNull
    private int rating;

    public Review toEntity(String mt20id) {
        return Review.builder()
                .mt20id(mt20id)     // 1. 파라미터로 받은 값 설정
                .userId(this.userId)    // 2. DTO가 가진 값 설정
                .rating(this.rating)    // 2. DTO가 가진 값 설정
                .review(this.review)  // 2. DTO가 가진 값 설정
                .createdAt(LocalDateTime.now())
                .build();
    }
}
