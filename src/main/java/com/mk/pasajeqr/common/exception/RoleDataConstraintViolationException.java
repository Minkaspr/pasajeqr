package com.mk.pasajeqr.common.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public class RoleDataConstraintViolationException extends ConstraintViolationException {
  public RoleDataConstraintViolationException(String message, Set<ConstraintViolation<?>> violations) {
    super(message, violations);
  }
}
