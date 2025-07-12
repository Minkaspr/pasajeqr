package com.mk.pasajeqr.auth;

import com.mk.pasajeqr.auth.request.LoginRQ;
import com.mk.pasajeqr.auth.request.RegisterRQ;
import com.mk.pasajeqr.auth.response.AuthRS;
import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.refresh_token.RefreshTokenRQ;
import com.mk.pasajeqr.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRQ request) {
        User newUser = authService.registerUser(request);
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Usuario registrado exitosamente",
                Map.of("userId", newUser),
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthRS>> login(@Valid @RequestBody LoginRQ request) {
        AuthRS result = authService.login(request.getEmail(), request.getPassword());
        ApiResponse<AuthRS> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Inicio de sesión exitoso",
                result,
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        authService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthRS>> refreshToken(@RequestBody RefreshTokenRQ request) {
        AuthRS response = authService.refreshAccessToken(request.getRefreshToken());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Token renovado exitosamente",
                response,
                null
        ));
    }
}
