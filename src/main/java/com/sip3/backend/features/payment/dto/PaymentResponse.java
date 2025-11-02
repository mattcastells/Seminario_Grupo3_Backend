package com.sip3.backend.features.payment.dto;

import com.sip3.backend.features.payment.model.PaymentMethodType;
import com.sip3.backend.features.payment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        String id,
        String serviceOrderId,
        String userId,
        String professionalId,
        BigDecimal amount,
        String currency,
        PaymentMethodType method,
        PaymentStatus status,
        String providerReference,
        String cardLastFour,
        String notes,
        Instant createdAt,
        Instant updatedAt,
        Instant processedAt
) {
}
