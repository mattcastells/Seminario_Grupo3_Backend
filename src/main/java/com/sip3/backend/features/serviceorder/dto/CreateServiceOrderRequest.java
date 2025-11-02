package com.sip3.backend.features.serviceorder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateServiceOrderRequest(
        @NotBlank(message = "El profesional es obligatorio")
        String professionalId,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 80)
        String contactName,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Formato de teléfono inválido")
        String contactPhone,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Correo inválido")
        String contactEmail,

        @NotBlank(message = "La dirección es obligatoria")
        String address,

        @NotBlank(message = "El tipo de servicio es obligatorio")
        String serviceType,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(min = 20, max = 2000)
        String description,

        @NotBlank(message = "La fecha preferida es obligatoria")
        String preferredDate,

        @NotNull(message = "El presupuesto es obligatorio")
        BigDecimal budget,

        String paymentPreference
) {
}
