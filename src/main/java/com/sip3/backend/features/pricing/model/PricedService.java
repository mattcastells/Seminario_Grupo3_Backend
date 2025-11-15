package com.sip3.backend.features.pricing.model;

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
@Document(collection = "priced_services")
public class PricedService {

    @Id
    private String id;

    private String professionalId; // ID del ProfessionalProfile

    private String trade; // Oficio (Plomero, Electricista, etc.)

    private String serviceName; // "Cambio de enchufe"

    private String description; // "Incluye mano de obra y materiales básicos"

    private BigDecimal basePrice; // Precio que define el profesional

    private boolean isCustom; // True si fue creado por el profesional, False si es de la lista "común"

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}