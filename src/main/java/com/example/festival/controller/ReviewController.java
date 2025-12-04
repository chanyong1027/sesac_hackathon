package com.example.festival.controller;

import com.example.festival.dto.ReviewRequestDto;
import com.example.festival.dto.ReviewResponseDto;
import com.example.festival.dto.ReviewUpdateRequestDto;
import com.example.festival.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/api/musicals/{mt20id}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @Valid @RequestBody ReviewRequestDto reviewRequestDto,
            @PathVariable String mt20id) {
        ReviewResponseDto reviewResponseDto = reviewService.addReview(reviewRequestDto, mt20id);

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponseDto);
    }

    @GetMapping("/api/musicals/{mt20id}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews(@PathVariable String mt20id) {
        List<ReviewResponseDto> reviewResponseDtos = reviewService.getAllReviews(mt20id);
        return  ResponseEntity.status(HttpStatus.OK).body(reviewResponseDtos);
    }

    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto updateRequestDto) {
        ReviewResponseDto updateResponseDto = reviewService.updateReview(reviewId, updateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponseDto);
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto reviewRequestDto) {

        reviewService.deleteReview(reviewId, reviewRequestDto.getUserId());
        return ResponseEntity.noContent().build();
    }
}
