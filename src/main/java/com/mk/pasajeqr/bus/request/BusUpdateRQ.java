package com.mk.pasajeqr.bus.request;

import com.mk.pasajeqr.utils.BusStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusUpdateRQ {
    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 10, message = "La placa no debe exceder los 10 caracteres")
    private String plate;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50, message = "El modelo no debe exceder los 50 caracteres")
    private String model;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;

    @NotNull(message = "El estado del bus es obligatorio")
    private BusStatus status;
}
