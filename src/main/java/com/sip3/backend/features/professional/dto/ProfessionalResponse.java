package com.sip3.backend.features.professional.dto;

import com.sip3.backend.features.professional.model.VerificationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record   ProfessionalResponse(
        String id,
        String userId,
        String displayName,
        String profession,
        String summary,
        String biography,
        int experienceYears,
        List<String> services,
        List<String> tags,
        Double rating,
        Integer reviewsCount,
        Double distanceKm,
        String address,
        BigDecimal minRate,
        BigDecimal maxRate,
        String avatarUrl,
        String contactEmail,
        String contactPhone,
        List<String> paymentMethods,
        List<String> availableJobs,
        VerificationStatus verificationStatus,
        boolean active,
        boolean featured,
        Instant createdAt,
        Instant updatedAt
) {
}
