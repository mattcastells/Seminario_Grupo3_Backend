package com.sip3.backend.features.professional.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record UpdateProfessionalRequest(
        String displayName,
        String profession,
        @Size(max = 180)
        String summary,
        @Size(max = 5000)
        String biography,
        @PositiveOrZero
        Integer experienceYears,
        List<String> services,
        List<String> tags,
        Double distanceKm,
        String address,
        BigDecimal minRate,
        BigDecimal maxRate,
        @Email
        String contactEmail,
        String contactPhone,
        List<String> paymentMethods,
        List<String> availableJobs,
        Boolean active,
        Boolean featured,
        Boolean faceVerified,
        Boolean dniFrontVerified,
        Boolean dniBackVerified
) {
}
