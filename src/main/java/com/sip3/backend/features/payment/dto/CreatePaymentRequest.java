package com.sip3.backend.features.payment.dto;

import com.sip3.backend.features.payment.model.PaymentMethodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotBlank
        String serviceOrderId,

        @NotNull
        BigDecimal amount,

        @NotBlank
        String currency,

        @NotNull
        PaymentMethodType method,

        String professionalId,

        @Size(max = 19)
        String cardNumber,

        String cardHolderName,

        @Pattern(regexp = "^$|^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Formato MM/AA")
        String cardExpiry,

        @Size(max = 4)
        String cardCvv
) {
}
