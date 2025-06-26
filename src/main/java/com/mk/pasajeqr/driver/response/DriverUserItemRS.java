package com.mk.pasajeqr.driver.response;

import com.mk.pasajeqr.driver.Driver;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DriverUserItemRS {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private Boolean status;
    private LocalDateTime createdAt;

    public DriverUserItemRS(Driver driver) {
        this.id = driver.getUser().getId();
        this.firstName = driver.getUser().getFirstName();
        this.lastName = driver.getUser().getLastName();
        this.dni = driver.getUser().getDni();
        this.email = driver.getUser().getEmail();
        this.status = driver.getUser().getStatus();
        this.createdAt = driver.getUser().getCreatedAt();
    }
}
