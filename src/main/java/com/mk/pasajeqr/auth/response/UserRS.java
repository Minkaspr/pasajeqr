package com.mk.pasajeqr.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRS {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
