package com.mk.pasajeqr.utils;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRQ {
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String currentPassword;

    @ValidPassword
    private String newPassword;
}
