package com.mk.pasajeqr.auth;

import com.mk.pasajeqr.auth.request.LoginRequest;
import com.mk.pasajeqr.auth.request.RegisterRequest;
import com.mk.pasajeqr.auth.response.AuthResponse;
import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserService;
import com.mk.pasajeqr.user.request.UserRegisterRequest;
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
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest request) {
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
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse result = authService.login(request.getEmail(), request.getPassword());
        ApiResponse<AuthResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Inicio de sesión exitoso",
                result,
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}
