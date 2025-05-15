package com.mk.pasajeqr.auth;

import com.mk.pasajeqr.auth.request.RegisterRequest;
import com.mk.pasajeqr.user.User;

public interface AuthService {
    User registerUser(RegisterRequest request);

    String login(String email, String password);

    void logout(String token);
}
