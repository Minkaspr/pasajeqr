package com.mk.pasajeqr.trip.response;

import com.mk.pasajeqr.trip.Trip;
import com.mk.pasajeqr.utils.ServiceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TripItemRS {
    private Long id;
    private String code;
    private String busPlate;
    private String driverName;
    private String originStopName;
    private String destinationStopName;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private ServiceStatus status;

    public TripItemRS(Trip service) {
        this.id = service.getId();
        this.code = service.getCode();
        this.busPlate = service.getBus().getPlate();
        this.driverName = service.getDriver().getFirstName() + " " + service.getDriver().getLastName();
        this.originStopName = service.getOriginStop().getName();
        this.destinationStopName = service.getDestinationStop().getName();
        this.departureDate = service.getDepartureDate();
        this.departureTime = service.getDepartureTime();
        this.status = service.getStatus();
    }
}
