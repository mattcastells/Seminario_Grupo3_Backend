package com.sip3.backend.features.professional.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "professionals")
public class ProfessionalProfile {

    @Id
    private String id;

    private String userId;

    @TextIndexed
    private String displayName;

    @TextIndexed
    private String profession;

    private String summary;

    private String biography;

    private int experienceYears;

    private List<String> services;

    private List<String> tags;

    private Double rating;

    private Integer reviewsCount;

    private Double distanceKm;

    private String address;

    private BigDecimal minRate;

    private BigDecimal maxRate;

    private String avatarUrl;

    private String contactEmail;

    private String contactPhone;

    private List<String> paymentMethods;

    private List<String> availableJobs;

    private VerificationStatus verificationStatus;

    private boolean active;

    private boolean featured;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
