package com.sip3.backend.features.professional.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record CreateProfessionalRequest(
        @NotBlank(message = "El usuario asociado es obligatorio")
        String userId,

        @NotBlank(message = "El nombre a mostrar es obligatorio")
        String displayName,

        @NotBlank(message = "La profesión es obligatoria")
        String profession,

        @Size(max = 180, message = "El resumen debe tener hasta 180 caracteres")
        String summary,

        @Size(max = 5000, message = "La biografía es demasiado extensa")
        String biography,

        @PositiveOrZero
        int experienceYears,

        @NotNull
        List<String> services,

        List<String> tags,

        Double distanceKm,

        String address,

        BigDecimal minRate,

        BigDecimal maxRate,

        @Email(message = "Correo inválido")
        String contactEmail,

        String contactPhone,

        List<String> paymentMethods,

        List<String> availableJobs
) {
}
