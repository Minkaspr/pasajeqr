package com.mk.pasajeqr.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRS {
    private String token;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
    private UserRS user;
}
