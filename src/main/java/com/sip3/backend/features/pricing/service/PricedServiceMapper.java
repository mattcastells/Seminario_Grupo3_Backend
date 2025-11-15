package com.sip3.backend.features.pricing.service;

import com.sip3.backend.features.pricing.dto.CreatePricedServiceRequest;
import com.sip3.backend.features.pricing.dto.PricedServiceResponse;
import com.sip3.backend.features.pricing.model.PricedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PricedServiceMapper {

    private final CommissionService commissionService;

    public PricedService toEntity(CreatePricedServiceRequest request) {
        return PricedService.builder()
                .professionalId(request.professionalId())
                .serviceName(request.serviceName())
                .description(request.description())
                .basePrice(request.basePrice())
                .isCustom(request.isCustom())
                .build();
    }

    public PricedServiceResponse toResponse(PricedService service) {
        BigDecimal basePrice = service.getBasePrice();
        BigDecimal commission = commissionService.calculateCommission(basePrice);
        BigDecimal finalPrice = commissionService.calculateFinalPrice(basePrice);

        return new PricedServiceResponse(
                service.getId(),
                service.getProfessionalId(),
                service.getServiceName(),
                service.getDescription(),
                basePrice,
                commission,
                finalPrice,
                service.isCustom(),
                service.getCreatedAt()
        );
    }
}