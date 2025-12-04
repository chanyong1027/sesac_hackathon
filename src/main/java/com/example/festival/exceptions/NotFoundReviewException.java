package com.example.festival.exceptions;

public class NotFoundReviewException extends RuntimeException{
    public NotFoundReviewException(String message){
        super(message);
    }
}
