package com.sip3.backend.features.pricing.service;

import com.sip3.backend.features.pricing.dto.CreatePricedServiceRequest;
import com.sip3.backend.features.pricing.dto.PricedServiceResponse;
import java.util.List;

public interface PricedServiceService {
    List<PricedServiceResponse> getServicesByProfessionalId(String professionalId);
    PricedServiceResponse createService(CreatePricedServiceRequest request);
    void deleteService(String serviceId, String professionalId);
}