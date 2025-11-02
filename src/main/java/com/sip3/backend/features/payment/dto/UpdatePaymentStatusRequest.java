package com.sip3.backend.features.payment.dto;

import com.sip3.backend.features.payment.model.PaymentStatus;

public record UpdatePaymentStatusRequest(
        PaymentStatus status,
        String providerReference,
        String notes
) {
}
