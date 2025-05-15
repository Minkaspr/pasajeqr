package com.mk.pasajeqr.user.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.mk.pasajeqr.user.dto.RegisterDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotNull(message = "El objeto userData es obligatorio")
    @Valid
    private RegisterDTO userData;

    @NotNull(message = "El objeto roleData es obligatorio")
    private JsonNode roleData;
}
