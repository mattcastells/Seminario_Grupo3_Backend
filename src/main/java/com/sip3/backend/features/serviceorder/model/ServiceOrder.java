package com.sip3.backend.features.serviceorder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "service_orders")
public class ServiceOrder {

    @Id
    private String id;

    private String userId;

    private String professionalId;

    private String contactName;

    private String contactPhone;

    private String contactEmail;

    private String address;

    private String serviceType;

    private String description;

    private String preferredDate;

    private BigDecimal budget;

    private ServiceOrderStatus status;

    private Instant scheduledAt;

    private String lastMessagePreview;

    private Instant lastMessageAt;

    private String paymentPreference;

    // Campos para completado bidireccional
    private Boolean completedByClient;
    private Integer clientRating; // 1-5
    private String clientComment;

    private Boolean completedByProfessional;
    private Integer professionalRating; // 1-5
    private String professionalComment;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
