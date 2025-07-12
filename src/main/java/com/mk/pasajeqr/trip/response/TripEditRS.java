package com.mk.pasajeqr.trip.response;

import com.mk.pasajeqr.utils.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TripEditRS {
    private Long id;
    private Long busId;
    private Long driverId;
    private Long originStopId;
    private Long destinationStopId;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    private ServiceStatus status;
    private String code;
}
