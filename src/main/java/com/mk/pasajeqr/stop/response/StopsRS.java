package com.mk.pasajeqr.stop.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopsRS {
    private List<StopItemRS> stops;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
