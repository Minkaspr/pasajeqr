package com.mk.pasajeqr.stop.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mk.pasajeqr.stop.Stop;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StopDetailRS {

    private Long id;
    private String name;
    private boolean terminal;
    private LocalDate createdAt;

    public StopDetailRS(Stop stop) {
        this.id = stop.getId();
        this.name = stop.getName();
        this.terminal = stop.isTerminal();
        this.createdAt = stop.getCreatedAt();
    }
}
