package com.mk.pasajeqr.bus.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusesRS {
    private List<BusItemRS> buses;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}