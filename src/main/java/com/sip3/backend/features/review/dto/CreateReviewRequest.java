package com.sip3.backend.features.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateReviewRequest(
        @NotBlank
        String professionalId,

        @Min(1)
        @Max(5)
        int rating,

        @NotBlank
        String comment
) {
}
