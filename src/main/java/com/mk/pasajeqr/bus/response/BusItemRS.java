package com.mk.pasajeqr.bus.response;

import com.mk.pasajeqr.bus.Bus;
import com.mk.pasajeqr.utils.BusStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BusItemRS {
    private Long id;
    private String plate;
    private String model;
    private Integer capacity;
    private BusStatus status;
    private LocalDateTime createdAt;

    public BusItemRS(Bus bus) {
        this.id = bus.getId();
        this.plate = bus.getPlate();
        this.model = bus.getModel();
        this.capacity = bus.getCapacity();
        this.status = bus.getStatus();
        this.createdAt = bus.getCreatedAt();
    }
}