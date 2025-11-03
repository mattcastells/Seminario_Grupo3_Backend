package com.sip3.backend.features.review.dto;

import java.time.Instant;

public record ReviewReplyResponse(
        String id,
        String reviewId,
        String professionalId,
        String professionalName,
        String reply,
        Instant createdAt,
        Instant updatedAt
) {
}

