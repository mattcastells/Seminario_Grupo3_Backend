package com.sip3.backend.features.review.service;

import com.sip3.backend.features.review.dto.CreateReviewRequest;
import com.sip3.backend.features.review.dto.ReviewReplyResponse;
import com.sip3.backend.features.review.dto.ReviewResponse;
import com.sip3.backend.features.review.model.Review;
import com.sip3.backend.features.review.model.ReviewReply;
import com.sip3.backend.features.review.repository.ReviewReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final ReviewReplyRepository replyRepository;

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
        // Buscar si existe una respuesta para esta review
        ReviewReplyResponse replyResponse = replyRepository.findByReviewId(review.getId())
                .map(this::mapReplyToResponse)
                .orElse(null);

        return new ReviewResponse(
                review.getId(),
                review.getProfessionalId(),
                review.getUserId(),
                review.getUserDisplayName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                replyResponse
        );
    }

    private ReviewReplyResponse mapReplyToResponse(ReviewReply reply) {
        return new ReviewReplyResponse(
                reply.getId(),
                reply.getReviewId(),
                reply.getProfessionalId(),
                reply.getProfessionalName(),
                reply.getReply(),
                reply.getCreatedAt(),
                reply.getUpdatedAt()
        );
    }
}
