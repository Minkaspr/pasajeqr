package com.mk.pasajeqr.passenger.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeRQ {

    @NotNull(message = "El monto de recarga es obligatorio")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor que 0")
    private BigDecimal amount;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;
}
