package com.example.festival.service;

import com.example.festival.domain.Review;
import com.example.festival.dto.ReviewRequestDto;
import com.example.festival.dto.ReviewResponseDto;
import com.example.festival.dto.ReviewUpdateRequestDto;
import com.example.festival.exceptions.NotFoundReviewException;
import com.example.festival.exceptions.ReviewAceessException;
import com.example.festival.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewResponseDto addReview(ReviewRequestDto reviewRequestDto, String mt20id) {
        Review review = reviewRequestDto.toEntity(mt20id);
        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDto(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviews(String mt20id) {
        List<Review> reviews = reviewRepository.findAllByMt20id(mt20id);
        return reviews.stream()
                .map(ReviewResponseDto::new) // review -> new ResponseDto(review)와 같음
                .toList();
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto updateRequestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundReviewException("존재하지 않는 리뷰입니다."));

        if(!review.getUserId().equals(updateRequestDto.getUserId())) {
            throw new ReviewAceessException("리뷰 수정 권한이 없습니다.");
        }

        review.update(updateRequestDto.getRating(), updateRequestDto.getReview());

        return new ReviewResponseDto(review);
    }

    public void deleteReview(Long reviewId, String userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundReviewException("존재하지 않는 리뷰입니다."));

        if (!review.getUserId().equals(userId)) {
            throw new ReviewAceessException("리뷰 삭제 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }
}
