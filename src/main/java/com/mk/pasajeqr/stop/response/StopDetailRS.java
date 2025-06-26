package com.mk.pasajeqr.stop.response;

import com.mk.pasajeqr.stop.Stop;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StopDetailRS {

    private Long id;
    private String name;
    private LocalDate createdAt;

    public StopDetailRS(Stop stop) {
        this.id = stop.getId();
        this.name = stop.getName();
        this.createdAt = stop.getCreatedAt();
    }
}
