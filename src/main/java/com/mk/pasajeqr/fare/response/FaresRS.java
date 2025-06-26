package com.mk.pasajeqr.fare.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaresRS {
    private List<FareItemRS> fares;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
