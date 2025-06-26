package com.mk.pasajeqr.utils;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkDeleteRQ {

    @NotEmpty(message = "La lista de IDs no puede estar vacía.")
    @Size(max = 20, message = "Solo se pueden eliminar hasta 20 elementos por operación.")
    private List<@NotNull @Min(value = 1, message = "Los IDs deben ser mayores o iguales a 1") Long> ids;
}