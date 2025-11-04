package com.sip3.backend.features.review.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateReplyRequest(
        @NotBlank(message = "La respuesta no puede estar vacía")
        String reply
) {
}

