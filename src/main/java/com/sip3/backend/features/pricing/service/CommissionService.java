package com.sip3.backend.features.pricing.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CommissionService {

    // Comisión fija del 15% por ahora
    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.15");

    /**
     * Calcula la comisión a sumar al precio base.
     */
    public BigDecimal calculateCommission(BigDecimal basePrice) {
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return basePrice.multiply(COMMISSION_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el precio final que verá el cliente.
     */
    public BigDecimal calculateFinalPrice(BigDecimal basePrice) {
        if (basePrice == null) {
            return null;
        }
        BigDecimal commission = calculateCommission(basePrice);
        return basePrice.add(commission);
    }
}