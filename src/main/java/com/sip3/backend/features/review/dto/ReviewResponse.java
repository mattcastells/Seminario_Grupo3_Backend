package com.sip3.backend.features.review.dto;

import java.time.Instant;

public record ReviewResponse(
        String id,
        String professionalId,
        String userId,
        String userDisplayName,
        int rating,
        String comment,
        Instant createdAt
) {
}
