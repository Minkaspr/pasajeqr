package com.mk.pasajeqr.fare.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FareCreateRQ {

    @NotBlank(message = "El código de tarifa es obligatorio")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    private String code;

    @NotNull(message = "El ID del paradero de origen es obligatorio")
    private Long originStopId;

    @NotNull(message = "El ID del paradero de destino es obligatorio")
    private Long destinationStopId;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El precio debe ser mayor que 0")
    @Digits(integer = 5, fraction = 2, message = "El precio debe tener hasta 5 dígitos enteros y 2 decimales")
    private BigDecimal price;
}

