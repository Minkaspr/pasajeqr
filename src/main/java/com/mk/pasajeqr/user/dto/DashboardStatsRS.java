package com.mk.pasajeqr.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsRS {
    // Pasajeros
    private long totalPassengers;
    private double passengerGrowth;
    private long todayPassengers;
    private long yesterdayPassengers;
    private long weeklyPassengers;

    // Conductores
    private long totalDrivers;
    private double driverGrowth;
    private long todayDrivers;
    private long yesterdayDrivers;
    private long weeklyDrivers;

    // Vehículos
    private long totalVehicles;
    private double vehicleGrowth;
    private long todayVehicles;
    private long yesterdayVehicles;
    private long weeklyVehicles;

    // Paraderos
    private long totalStops;
    private double stopGrowth;
    private long todayStops;
    private long yesterdayStops;
    private long weeklyStops;
}
