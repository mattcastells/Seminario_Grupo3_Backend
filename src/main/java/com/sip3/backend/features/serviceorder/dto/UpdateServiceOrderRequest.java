package com.sip3.backend.features.serviceorder.dto;

import com.sip3.backend.features.serviceorder.model.ServiceOrderStatus;

import java.math.BigDecimal;

public record UpdateServiceOrderRequest(
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
        Long scheduledAtEpoch,
        String lastMessagePreview
) {
}
