package com.sip3.backend.features.professional.service;

import com.sip3.backend.features.professional.dto.CreateProfessionalRequest;
import com.sip3.backend.features.professional.dto.ProfessionalResponse;
import com.sip3.backend.features.professional.dto.UpdateProfessionalRequest;
import com.sip3.backend.features.professional.model.ProfessionalProfile;
import com.sip3.backend.features.professional.model.VerificationStatus;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalMapper {

    public ProfessionalProfile toEntity(CreateProfessionalRequest request) {
        return ProfessionalProfile.builder()
                .userId(request.userId())
                .displayName(request.displayName())
                .profession(request.profession())
                .summary(request.summary())
                .biography(request.biography())
                .experienceYears(request.experienceYears())
                .services(request.services())
                .tags(request.tags())
                .distanceKm(request.distanceKm())
                .address(request.address())
                .minRate(request.minRate())
                .maxRate(request.maxRate())
                .contactEmail(request.contactEmail())
                .contactPhone(request.contactPhone())
                .paymentMethods(request.paymentMethods())
                .availableJobs(request.availableJobs())
                .verificationStatus(VerificationStatus.builder().build())
                .active(true)
                .featured(false)
                .build();
    }

    public ProfessionalResponse toResponse(ProfessionalProfile profile) {
        return new ProfessionalResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getDisplayName(),
                profile.getProfession(),
                profile.getSummary(),
                profile.getBiography(),
                profile.getExperienceYears(),
                profile.getServices(),
                profile.getTags(),
                profile.getRating(),
                profile.getReviewsCount(),
                profile.getDistanceKm(),
                profile.getAddress(),
                profile.getMinRate(),
                profile.getMaxRate(),
                profile.getContactEmail(),
                profile.getContactPhone(),
                profile.getPaymentMethods(),
                profile.getAvailableJobs(),
                profile.getVerificationStatus(),
                profile.isActive(),
                profile.isFeatured(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    public void updateEntity(ProfessionalProfile profile, UpdateProfessionalRequest request) {
        if (request.displayName() != null) {
            profile.setDisplayName(request.displayName());
        }
        if (request.profession() != null) {
            profile.setProfession(request.profession());
        }
        if (request.summary() != null) {
            profile.setSummary(request.summary());
        }
        if (request.biography() != null) {
            profile.setBiography(request.biography());
        }
        if (request.experienceYears() != null) {
            profile.setExperienceYears(request.experienceYears());
        }
        if (request.services() != null) {
            profile.setServices(request.services());
        }
        if (request.tags() != null) {
            profile.setTags(request.tags());
        }
        if (request.distanceKm() != null) {
            profile.setDistanceKm(request.distanceKm());
        }
        if (request.address() != null) {
            profile.setAddress(request.address());
        }
        if (request.minRate() != null) {
            profile.setMinRate(request.minRate());
        }
        if (request.maxRate() != null) {
            profile.setMaxRate(request.maxRate());
        }
        if (request.contactEmail() != null) {
            profile.setContactEmail(request.contactEmail());
        }
        if (request.contactPhone() != null) {
            profile.setContactPhone(request.contactPhone());
        }
        if (request.paymentMethods() != null) {
            profile.setPaymentMethods(request.paymentMethods());
        }
        if (request.availableJobs() != null) {
            profile.setAvailableJobs(request.availableJobs());
        }
        if (request.active() != null) {
            profile.setActive(request.active());
        }
        if (request.featured() != null) {
            profile.setFeatured(request.featured());
        }
        VerificationStatus status = profile.getVerificationStatus();
        if (status == null) {
            status = new VerificationStatus();
            profile.setVerificationStatus(status);
        }
        if (request.faceVerified() != null) {
            status.setFaceVerified(request.faceVerified());
        }
        if (request.dniFrontVerified() != null) {
            status.setDniFrontVerified(request.dniFrontVerified());
        }
        if (request.dniBackVerified() != null) {
            status.setDniBackVerified(request.dniBackVerified());
        }
    }
}
