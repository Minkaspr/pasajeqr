package com.mk.pasajeqr.admin.response;

import com.mk.pasajeqr.admin.Admin;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminUserItemRS {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDate birthDate;

    public AdminUserItemRS(Admin admin) {
        this.id = admin.getUser().getId();
        this.firstName = admin.getUser().getFirstName();
        this.lastName = admin.getUser().getLastName();
        this.dni = admin.getUser().getDni();
        this.email = admin.getUser().getEmail();
        this.status = admin.getUser().getStatus();
        this.createdAt = admin.getUser().getCreatedAt();
        this.birthDate = admin.getBirthDate();
    }
}
