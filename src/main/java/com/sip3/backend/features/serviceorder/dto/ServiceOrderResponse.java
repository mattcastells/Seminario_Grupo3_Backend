package com.sip3.backend.features.serviceorder.dto;

import com.sip3.backend.features.serviceorder.model.ServiceOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record ServiceOrderResponse(
        String id,
        String userId,
        String professionalId,
        String contactName,
        String contactPhone,
        String contactEmail,
        String address,
        String serviceType,
        String description,
        String preferredDate,
        BigDecimal budget,
        String paymentPreference,
        ServiceOrderStatus status,
        Instant scheduledAt,
        String lastMessagePreview,
        Instant lastMessageAt,
        Boolean completedByClient,
        Integer clientRating,
        String clientComment,
        Boolean completedByProfessional,
        Integer professionalRating,
        String professionalComment,
        Instant createdAt,
        Instant updatedAt
) {
}
