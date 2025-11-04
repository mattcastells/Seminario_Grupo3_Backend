package com.sip3.backend.features.serviceorder.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelServiceOrderRequest(
        @NotBlank(message = "Reason is required")
        String reason
) {
}
