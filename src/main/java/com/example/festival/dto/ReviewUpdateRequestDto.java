package com.example.festival.dto;

import com.example.festival.domain.Review;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequestDto {
    @NotNull
    private String userId;
    private Integer rating;
    private String review;
}
