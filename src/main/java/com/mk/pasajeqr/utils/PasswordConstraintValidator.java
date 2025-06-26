package com.mk.pasajeqr.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    private static final int MIN_LENGTH = 8;
    // private static final String PATTERN = "^[A-Za-z\\d@#$%*_\\-]+$";
    private static final String PATTERN = "^[\\p{L}\\d@#$%*_\\-]+$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            buildMessage(context, "La contraseña es obligatoria.");
            return false;
        }

        if (password.length() < MIN_LENGTH) {
            buildMessage(context, "La contraseña debe tener al menos 8 caracteres.");
            return false;
        }

        if (!password.matches(PATTERN)) {
            buildMessage(context, "La contraseña solo puede contener letras, números y los símbolos: @ # $ % * _ -");
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            buildMessage(context, "La contraseña debe contener al menos una letra minúscula.");
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            buildMessage(context, "La contraseña debe contener al menos una letra mayúscula.");
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            buildMessage(context, "La contraseña debe contener al menos un número.");
            return false;
        }

        if (!password.matches(".*[@#$%*_\\-].*")) {
            buildMessage(context, "La contraseña debe contener al menos un símbolo: @ # $ % * _ -");
            return false;
        }

        return true;
    }

    private void buildMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
