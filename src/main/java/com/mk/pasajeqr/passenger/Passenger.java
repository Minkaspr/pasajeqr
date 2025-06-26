package com.mk.pasajeqr.passenger;

import com.mk.pasajeqr.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "passenger")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @DecimalMin(value = "0.00", inclusive = true, message = "El saldo no puede ser negativo")
    @Column(nullable = false)
    @NotNull(message = "El saldo no puede ser nulo")
    private BigDecimal balance;
}
