package com.sip3.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 30, message = "El usuario debe tener entre 3 y 30 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Solo letras, números y ._-")
        String username,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Correo inválido")
        String email,

        @Pattern(regexp = "^$|^\\+?\\d{7,15}$", message = "Teléfono inválido")
        String phone,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "El nombre completo es obligatorio")
        String fullName,

        boolean registerAsProfessional
) {
}
