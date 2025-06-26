package com.mk.pasajeqr.stop.response;

import com.mk.pasajeqr.stop.Stop;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StopItemRS {

    private Long id;
    private String name;
    private LocalDate createdAt;

    public StopItemRS(Stop stop) {
        this.id = stop.getId();
        this.name = stop.getName();
        this.createdAt = stop.getCreatedAt();
    }
}
