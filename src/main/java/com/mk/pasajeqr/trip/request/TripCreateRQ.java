package com.mk.pasajeqr.trip.request;

import com.mk.pasajeqr.utils.ServiceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripCreateRQ {

    @NotNull(message = "El ID del bus es obligatorio")
    private Long busId;

    @NotNull(message = "El ID del conductor es obligatorio")
    private Long driverId;

    @NotNull(message = "El ID del paradero de origen es obligatorio")
    private Long originStopId;

    @NotNull(message = "El ID del paradero de destino es obligatorio")
    private Long destinationStopId;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDate departureDate;

    @NotNull(message = "La hora de salida es obligatoria")
    private LocalTime departureTime;

    private LocalDate arrivalDate;
    private LocalTime arrivalTime;

    @NotNull(message = "El estado del servicio es obligatorio")
    private ServiceStatus status;
}
