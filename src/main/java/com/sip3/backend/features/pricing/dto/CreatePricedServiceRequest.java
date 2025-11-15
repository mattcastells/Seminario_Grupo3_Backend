package com.sip3.backend.features.pricing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePricedServiceRequest(
        @NotBlank
        String professionalId,

        @NotBlank
        @Size(min = 3, max = 100)
        String serviceName,

        @Size(max = 250)
        String description,

        @NotNull
        @PositiveOrZero
        BigDecimal basePrice,

        @NotNull
        boolean isCustom
) {}