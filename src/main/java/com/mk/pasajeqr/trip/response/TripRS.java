package com.mk.pasajeqr.trip.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripRS {
    private List<TripItemRS> services;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
