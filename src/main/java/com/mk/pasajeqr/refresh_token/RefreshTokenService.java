package com.mk.pasajeqr.refresh_token;

import com.mk.pasajeqr.auth.response.AuthRS;
import com.mk.pasajeqr.user.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    boolean isValid(RefreshToken token);
    void revokeByUser(User user);
    void revoke(String tokenStr);
    Optional<RefreshToken> findByToken(String token);
}
