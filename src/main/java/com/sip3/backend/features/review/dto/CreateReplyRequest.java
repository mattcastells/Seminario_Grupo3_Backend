package com.sip3.backend.features.review.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateReplyRequest(
        @NotBlank(message = "El ID de la review es requerido")
        String reviewId,

        @NotBlank(message = "La respuesta no puede estar vacía")
        String reply
) {
}

