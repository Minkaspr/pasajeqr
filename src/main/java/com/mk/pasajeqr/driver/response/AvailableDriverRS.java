package com.mk.pasajeqr.driver.response;

import com.mk.pasajeqr.driver.Driver;
import lombok.Data;

@Data
public class AvailableDriverRS {
    private Long id;
    private String firstName;
    private String lastName;

    public AvailableDriverRS(Driver driver) {
        this.id = driver.getUser().getId();
        this.firstName = driver.getUser().getFirstName();
        this.lastName = driver.getUser().getLastName();
    }
}
