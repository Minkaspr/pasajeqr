package com.mk.pasajeqr.balance_transaction.request;

import com.mk.pasajeqr.utils.TransactionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceTransactionCreateRQ {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Min(value = 1, message = "El ID del usuario debe ser mayor que 0")
    private Long userId;

    @NotNull(message = "El tipo de transacción es obligatorio")
    private TransactionType type;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor que 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener hasta 10 dígitos enteros y 2 decimales")
    private BigDecimal amount;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;
}
