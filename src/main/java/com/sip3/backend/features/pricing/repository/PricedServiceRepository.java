package com.sip3.backend.features.pricing.repository;

import com.sip3.backend.features.pricing.model.PricedService;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PricedServiceRepository extends MongoRepository<PricedService, String> {
    List<PricedService> findByProfessionalId(String professionalId);
}