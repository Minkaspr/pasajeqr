package com.mk.pasajeqr.common.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorResponse {
    private String field;
    private List<String> messages;
}
