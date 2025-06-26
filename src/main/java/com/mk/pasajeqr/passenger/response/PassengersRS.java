package com.mk.pasajeqr.passenger.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengersRS {
    private List<PassengerUserItemRS> passengers;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
