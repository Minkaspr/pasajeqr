package com.mk.pasajeqr.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;     // El código de estado HTTP de la respuesta.
    private String message; // Un mensaje descriptivo sobre la respuesta.
    private T data;    // Los datos que se devuelven en la respuesta, si los hay.
    private Object  errors;  // Cualquier error que ocurrió durante el procesamiento de la solicitud.
}
