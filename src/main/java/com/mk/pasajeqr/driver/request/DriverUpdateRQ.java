package com.mk.pasajeqr.driver.request;

import com.mk.pasajeqr.utils.DriverStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverUpdateRQ {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String lastName;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotNull(message = "El estado del usuario es obligatorio")
    private Boolean userStatus;

    @NotBlank(message = "El número de licencia es obligatorio")
    @Size(max = 20, message = "El número de licencia no debe exceder los 20 caracteres")
    private String licenseNumber;

    @NotNull(message = "El estado del conductor es obligatorio")
    private DriverStatus status;
}
