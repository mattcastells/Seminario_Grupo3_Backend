package com.sip3.backend.features.professional.repository;

import com.sip3.backend.features.professional.model.ProfessionalProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfessionalRepository extends MongoRepository<ProfessionalProfile, String> {
    Optional<ProfessionalProfile> findByUserId(String userId);
}
