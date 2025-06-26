package com.mk.pasajeqr.admin.response;

import com.mk.pasajeqr.admin.Admin;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminDetailRS {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate birthDate;

    public AdminDetailRS(Admin admin) {
        this.id = admin.getUser().getId();
        this.firstName = admin.getUser().getFirstName();
        this.lastName = admin.getUser().getLastName();
        this.dni = admin.getUser().getDni();
        this.email = admin.getUser().getEmail();
        this.status = admin.getUser().getStatus();
        this.createdAt = admin.getUser().getCreatedAt();
        this.updatedAt = admin.getUser().getUpdatedAt();
        this.birthDate = admin.getBirthDate();
    }
}