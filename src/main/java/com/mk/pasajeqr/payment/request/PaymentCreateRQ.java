package com.mk.pasajeqr.payment.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateRQ {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Long serviceId;

    @NotNull(message = "El ID del paradero de origen es obligatorio")
    private Long originStopId;

    @NotNull(message = "El ID del paradero de destino es obligatorio")
    private Long destinationStopId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;

    @NotNull(message = "El ID del usuario que registra el pago es obligatorio")
    private Long registeredBy;
}
