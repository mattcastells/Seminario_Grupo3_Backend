package com.sip3.backend.features.payment.service;

import com.sip3.backend.features.payment.dto.CreatePaymentRequest;
import com.sip3.backend.features.payment.dto.PaymentResponse;
import com.sip3.backend.features.payment.model.Payment;
import com.sip3.backend.features.payment.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentMapper {

    public Payment toEntity(String userId, CreatePaymentRequest request) {
        String cardLastFour = null;
        if (request.cardNumber() != null && !request.cardNumber().isBlank()) {
            String digits = request.cardNumber().replaceAll("\\s", "");
            if (digits.length() >= 4) {
                cardLastFour = digits.substring(digits.length() - 4);
            }
        }
        return Payment.builder()
                .serviceOrderId(request.serviceOrderId())
                .userId(userId)
                .professionalId(request.professionalId())
                .amount(request.amount())
                .currency(request.currency())
                .method(request.method())
                .status(PaymentStatus.PROCESSING)
                .cardLastFour(cardLastFour)
                .build();
    }

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getServiceOrderId(),
                payment.getUserId(),
                payment.getProfessionalId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getProviderReference(),
                payment.getCardLastFour(),
                payment.getNotes(),
                payment.getCreatedAt(),
                payment.getUpdatedAt(),
                payment.getProcessedAt()
        );
    }

    public void markProcessed(Payment payment) {
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setProcessedAt(Instant.now());
    }
}
