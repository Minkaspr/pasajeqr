package com.mk.pasajeqr.auth;

import com.mk.pasajeqr.auth.request.RegisterRQ;
import com.mk.pasajeqr.auth.response.AuthRS;
import com.mk.pasajeqr.user.User;

public interface AuthService {
    User registerUser(RegisterRQ request);

    AuthRS login(String email, String password);

    void logout(String token);

    AuthRS refreshAccessToken(String refreshTokenStr);
}
