package com.mk.pasajeqr.passenger.request;

import com.mk.pasajeqr.utils.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerCreateRQ {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String lastName;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @ValidPassword
    private String password;

    @NotNull(message = "El saldo inicial no puede ser nulo")
    @DecimalMin(value = "0.00", inclusive = true, message = "El saldo no puede ser negativo")
    private BigDecimal balance;
}
