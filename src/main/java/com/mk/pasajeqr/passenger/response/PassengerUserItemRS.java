package com.mk.pasajeqr.passenger.response;

import com.mk.pasajeqr.passenger.Passenger;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PassengerUserItemRS {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private Boolean status;
    private LocalDateTime createdAt;
    private BigDecimal balance;

    public PassengerUserItemRS(Passenger passenger) {
        this.id = passenger.getUser().getId();
        this.firstName = passenger.getUser().getFirstName();
        this.lastName = passenger.getUser().getLastName();
        this.dni = passenger.getUser().getDni();
        this.email = passenger.getUser().getEmail();
        this.status = passenger.getUser().getStatus();
        this.createdAt = passenger.getUser().getCreatedAt();
        this.balance = passenger.getBalance();
    }
}
