package com.mk.pasajeqr.service;

import com.mk.pasajeqr.bus.Bus;
import com.mk.pasajeqr.stop.Stop;
import com.mk.pasajeqr.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "service")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @ManyToOne
    @JoinColumn(name = "origin_stop_id", nullable = false)
    private Stop originStop;

    @ManyToOne
    @JoinColumn(name = "destination_stop_id", nullable = false)
    private Stop destinationStop;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = true)
    private LocalDateTime arrivalTime;
}
