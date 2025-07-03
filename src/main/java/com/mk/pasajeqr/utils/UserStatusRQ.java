package com.mk.pasajeqr.utils;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusRQ {
    @NotNull(message = "El estado activo/inactivo es obligatorio")
    private Boolean active;
}
