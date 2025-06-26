package com.mk.pasajeqr.trip.response;

import com.mk.pasajeqr.trip.Trip;
import com.mk.pasajeqr.utils.ServiceStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TripDetailRS {
    private Long id;
    private String code;
    private String busPlate;
    private String driverName;
    private String originStopName;
    private String destinationStopName;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private ServiceStatus status;

    public TripDetailRS(Trip service) {
        this.id = service.getId();
        this.code = service.getCode();
        this.busPlate = service.getBus().getPlate();
        this.driverName = service.getDriver().getFirstName() + " " + service.getDriver().getLastName();
        this.originStopName = service.getOriginStop().getName();
        this.destinationStopName = service.getDestinationStop().getName();

        this.departureDateTime = LocalDateTime.of(
                service.getDepartureDate(),
                service.getDepartureTime()
        );

        // La llegada puede ser opcional
        if (service.getArrivalDate() != null && service.getArrivalTime() != null) {
            this.arrivalDateTime = LocalDateTime.of(
                    service.getArrivalDate(),
                    service.getArrivalTime()
            );
        } else {
            this.arrivalDateTime = null;
        }

        this.status = service.getStatus();
    }
}

