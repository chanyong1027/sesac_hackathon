package com.example.festival.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mt20id;
    private String userId;
    private int rating;
    private LocalDateTime createdAt;
    @Lob
    private String review;

    public void update(Integer rating, String review) {
        if (rating != null) {
            this.rating = rating;
        }

        if (review != null) {
            this.review = review;
        }
    }
}
