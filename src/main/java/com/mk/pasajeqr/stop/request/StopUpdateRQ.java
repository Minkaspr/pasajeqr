package com.mk.pasajeqr.stop.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopUpdateRQ {

    @NotBlank(message = "El nombre del paradero es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del paradero debe tener entre 2 y 100 caracteres")
    private String name;

    private boolean terminal;
}
