package com.mk.pasajeqr.jwt_config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public boolean validateToken(String token) {
        try {
            jwtTokenProvider.validate(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        Claims claims = jwtTokenProvider.validate(token);
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = jwtTokenProvider.validate(token);
        return claims.get("role", String.class);
    }
}
