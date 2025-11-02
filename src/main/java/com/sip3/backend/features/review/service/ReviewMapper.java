package com.sip3.backend.features.review.service;

import com.sip3.backend.features.review.dto.CreateReviewRequest;
import com.sip3.backend.features.review.dto.ReviewResponse;
import com.sip3.backend.features.review.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(String userId, String userDisplayName, CreateReviewRequest request) {
        return Review.builder()
                .professionalId(request.professionalId())
                .userId(userId)
                .userDisplayName(userDisplayName)
                .rating(request.rating())
                .comment(request.comment())
                .build();
    }

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProfessionalId(),
                review.getUserId(),
                review.getUserDisplayName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
