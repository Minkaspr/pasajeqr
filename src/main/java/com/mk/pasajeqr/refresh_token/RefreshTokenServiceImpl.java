package com.mk.pasajeqr.refresh_token;

import com.mk.pasajeqr.jwt_config.JwtProperties;
import com.mk.pasajeqr.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .exp(now.plus(Duration.ofMillis(jwtProperties.getRefreshExpirationMs())))
                .revoked(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return refreshTokenRepository.save(token);
    }

    @Override
    public boolean isValid(RefreshToken token) {
        return !token.isRevoked() && token.getExp().isAfter(LocalDateTime.now());
    }

    @Override
    public void revokeByUser(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUser(user);
        for (RefreshToken token : tokens) {
            if (!token.isRevoked()) {
                token.setRevoked(true);
                token.setUpdatedAt(LocalDateTime.now());
            }
        }
    }

    @Override
    public void revoke(String tokenStr) {
        refreshTokenRepository.findByToken(tokenStr).ifPresent(token -> {
            if (!token.isRevoked()) {
                token.setRevoked(true);
                token.setUpdatedAt(LocalDateTime.now());
            }
        });
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
