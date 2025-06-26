package com.mk.pasajeqr.fare;

import com.mk.pasajeqr.stop.Stop;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "fare")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Fare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "El código de tarifa es obligatorio")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_stop_id", nullable = false)
    @NotNull(message = "El paradero de origen es obligatorio")
    private Stop originStop;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_stop_id", nullable = false)
    @NotNull(message = "El paradero de destino es obligatorio")
    private Stop destinationStop;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El precio debe ser mayor que 0")
    @Digits(integer = 5, fraction = 2, message = "El precio debe tener hasta 5 dígitos enteros y 2 decimales")
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;
}

