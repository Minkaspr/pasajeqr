package com.mk.pasajeqr.driver.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriversRS<T> {
    private List<T> drivers;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
