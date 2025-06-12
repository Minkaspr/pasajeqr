package com.mk.pasajeqr.driver.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BulkDeleteRequest {
    @NotEmpty(message = "La lista de IDs no puede estar vacía.")
    @Size(max = 20, message = "Solo se pueden eliminar hasta 20 conductores por operación.")
    private List<@NotNull @Min(value = 1, message = "Los IDs deben ser mayores o iguales a 1") Long> driverIds;
}
