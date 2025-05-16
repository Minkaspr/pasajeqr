package com.mk.pasajeqr.auth;

import com.mk.pasajeqr.auth.request.RegisterRequest;
import com.mk.pasajeqr.auth.response.AuthResponse;
import com.mk.pasajeqr.user.User;

public interface AuthService {
    User registerUser(RegisterRequest request);

    AuthResponse login(String email, String password);

    void logout(String token);
}
