package com.mk.pasajeqr.auth.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Valid
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Pattern(
            regexp = "^[A-Za-z\\d@#\\$%*_\\-]+$",
            message = "La contraseña solo puede contener letras, números y los símbolos: @ # $ % * _ -"
    )
    @Valid
    private String password;
}
