package com.mk.pasajeqr.payment;

import com.mk.pasajeqr.trip.Trip;
import com.mk.pasajeqr.stop.Stop;
import com.mk.pasajeqr.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Trip service;

    @ManyToOne
    @JoinColumn(name = "origin_stop_id", nullable = false)
    private Stop originStop;

    @ManyToOne
    @JoinColumn(name = "destination_stop_id", nullable = false)
    private Stop destinationStop;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "registered_by")
    private User registeredBy;

    @Column(nullable = false, length = 32)
    private String paymentMethod;
}
