package com.sip3.backend.features.pricing.dto;

import java.math.BigDecimal;
import java.time.Instant;

// Este DTO único sirve para ambos:
// - El profesional (para ver su desglose)
// - El cliente (el frontend simplemente ocultará basePrice y commissionAmount)
public record PricedServiceResponse(
        String id,
        String professionalId,
        String serviceName,
        String description,
        BigDecimal basePrice,       // Precio del profesional
        BigDecimal commissionAmount,  // Comisión de la App
        BigDecimal finalPrice,      // Precio que ve el cliente
        boolean isCustom,
        Instant createdAt
) {}