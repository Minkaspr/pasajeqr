package com.mk.pasajeqr.balance_transaction;

import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.utils.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_transaction")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "El tipo de transacción es obligatorio")
    private TransactionType type;


    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor que 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener hasta 10 dígitos enteros y 2 decimales")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "La fecha de transacción es obligatoria")
    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;
}
