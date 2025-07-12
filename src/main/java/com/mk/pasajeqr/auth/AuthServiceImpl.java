package com.mk.pasajeqr.auth;

import com.mk.pasajeqr.auth.request.RegisterRQ;
import com.mk.pasajeqr.auth.response.AuthRS;
import com.mk.pasajeqr.auth.response.UserRS;
import com.mk.pasajeqr.common.exception.UnauthorizedException;
import com.mk.pasajeqr.passenger.Passenger;
import com.mk.pasajeqr.passenger.PassengerRepository;
import com.mk.pasajeqr.jwt_config.JwtTokenProvider;
import com.mk.pasajeqr.refresh_token.RefreshToken;
import com.mk.pasajeqr.refresh_token.RefreshTokenService;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserRepository;
import com.mk.pasajeqr.utils.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @Override
    @Transactional
    public User  registerUser(RegisterRQ request) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dni(request.getDni())
                .email(request.getEmail())
                .password(encryptedPassword)
                .role(RoleType.PASSENGER)
                .status(true)
                .build();

        User newUser = userRepository.save(user);

        Passenger passenger = new Passenger();
        passenger.setBalance(BigDecimal.ZERO);
        passenger.setUser(newUser);

        Passenger newPassenger = passengerRepository.save(passenger);
        return newUser;
    }

    @Override
    public AuthRS login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        // Crear access token (JWT)
        String accessToken = jwtTokenProvider.generateAccessToken(user);

        // Crear refresh token (en BD)
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // Respuesta
        return AuthRS.builder()
                .token(accessToken)
                .refreshToken(refreshToken.getToken()) // valor UUID generado
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessExpirationMs() / 1000)
                .user(UserRS.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .role(user.getRole().name())
                        .build())
                .build();
    }

    @Override
    public void logout(String refreshTokenStr) {
        refreshTokenService.revoke(refreshTokenStr);
    }

    @Override
    public AuthRS refreshAccessToken(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr)
                .orElseThrow(() -> new UnauthorizedException("Refresh token inválido"));

        if (!refreshTokenService.isValid(refreshToken)) {
            refreshTokenService.revoke(refreshTokenStr);
            throw new UnauthorizedException("Refresh token caducado o revocado");
        }

        User user = refreshToken.getUser();

        // Revocar el refresh token actual (rotación segura)
        refreshTokenService.revoke(refreshTokenStr);

        // Generar nuevos tokens
        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return AuthRS.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessExpirationMs() / 1000)
                .user(UserRS.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .role(user.getRole().name())
                        .build())
                .build();
    }
}
