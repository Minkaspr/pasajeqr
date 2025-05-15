package com.mk.pasajeqr.utils;

public enum DriverStatus {
    AVAILABLE,      // Presente y sin asignación
    ON_SERVICE,     // Asignado a un servicio actualmente
    OFF_DUTY,       // Día libre
    SICK_LEAVE      // Ausente por enfermedad u otra razón
}
