package com.mk.pasajeqr.passenger.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerLookupRS {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private BigDecimal balance;
    private Boolean active;
}