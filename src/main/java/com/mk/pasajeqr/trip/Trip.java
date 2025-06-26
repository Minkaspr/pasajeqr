package com.mk.pasajeqr.trip;

import com.mk.pasajeqr.bus.Bus;
import com.mk.pasajeqr.stop.Stop;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.utils.ServiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "trip")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "El código de servicio es obligatorio")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bus_id", nullable = false)
    @NotNull(message = "El bus es obligatorio")
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    @NotNull(message = "El conductor es obligatorio")
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_stop_id", nullable = false)
    @NotNull(message = "El paradero de origen es obligatorio")
    private Stop originStop;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_stop_id", nullable = false)
    @NotNull(message = "El paradero de destino es obligatorio")
    private Stop destinationStop;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDate departureDate;

    @NotNull(message = "La hora de salida es obligatoria")
    private LocalTime departureTime;

    private LocalDate arrivalDate;

    private LocalTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El estado del servicio es obligatorio")
    private ServiceStatus status;
}
