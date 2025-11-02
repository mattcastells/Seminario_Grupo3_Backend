package com.sip3.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserProfileUpdateRequest(
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 60, message = "Debe tener entre 3 y 60 caracteres")
        String fullName,

        @NotBlank(message = "La ubicación es obligatoria")
        String location,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Formato de teléfono inválido")
        String phone,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Correo inválido")
        String email,

        List<String> preferredPaymentMethods
) {
}
