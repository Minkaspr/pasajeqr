package com.mk.pasajeqr.trip;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class TripQrSimpleTokenService {
    private static final String SECRET_KEY = "1234567890123456"; // 16 caracteres (AES-128)
    private static final String ALGORITHM = "AES";

    public String encryptTripId(Long tripId) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(tripId.toString().getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted); // QR-safe
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar el tripId", e);
        }
    }

    public Long decryptToken(String token) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getUrlDecoder().decode(token));
            return Long.parseLong(new String(decrypted));
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido o corrupto");
        }
    }
}