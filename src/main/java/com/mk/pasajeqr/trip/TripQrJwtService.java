package com.mk.pasajeqr.trip;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class TripQrJwtService {
    private static final String SECRET_KEY = "l4_cl4v3_d3l_qr_x_mk_b0ss_Nimbvs2025"; // al menos 32 caracteres
    private static final long EXPIRATION_MILLIS = 1000 * 60 * 10; // 10 minutos

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(Trip trip) {
        Date now = new Date();
        LocalDateTime departureDateTime = LocalDateTime.of(trip.getDepartureDate(), trip.getDepartureTime());
        ZonedDateTime zonedDeparture = departureDateTime.atZone(ZoneId.systemDefault());
        Date expiration = Date.from(zonedDeparture.plusDays(1).toInstant()); // 1 día después de la salida

        return Jwts.builder()
                .setSubject("QR_TRIP")
                .claim("tripId", trip.getId())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long validateTokenAndGetTripId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("tripId", Long.class);
        } catch (JwtException e) {
            // token inválido o expirado
            throw new IllegalArgumentException("Token inválido o expirado");
        }
    }
}
