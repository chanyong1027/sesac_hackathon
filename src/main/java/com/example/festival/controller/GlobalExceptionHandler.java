package com.example.festival.controller;

import com.example.festival.exceptions.NotFoundReviewException;
import com.example.festival.exceptions.ReviewAceessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ReviewAceessException.class)
    public ResponseEntity<String> handleReviewUpdateAccessException(ReviewAceessException ex){
        return new  ResponseEntity<>("Review를 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundReviewException.class)
    public ResponseEntity<String> handleNotFoundReviewException(NotFoundReviewException ex){
        return new  ResponseEntity<>("Review를 찾지 못했습니다.", HttpStatus.NOT_FOUND);
    }

}
