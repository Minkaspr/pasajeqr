package com.mk.pasajeqr.driver.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeStatusRequest {
    @NotNull(message = "El estado activo/inactivo es obligatorio")
    private Boolean active;
}
