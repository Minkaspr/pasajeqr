package com.mk.pasajeqr.common.exception;

import com.mk.pasajeqr.common.response.ApiResponse;
import com.mk.pasajeqr.common.response.FieldErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    // Maneja errores de validación de @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        log.warn("Validación fallida: {}", ex.getMessage());

        Map<String, List<String>> errorMap = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMap.computeIfAbsent(fieldError.getField(), key -> new ArrayList<>())
                    .add(fieldError.getDefaultMessage());
        }

        List<FieldErrorResponse> fieldErrors = errorMap.entrySet().stream()
                .map(e -> new FieldErrorResponse(e.getKey(), e.getValue()))
                .toList();

        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Errores de validación",
                null,
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    // Maneja violaciones de integridad de base de datos (claves duplicadas, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Violación de integridad de datos: {}", ex.getMessage(), ex);

        String defaultMessage = "El valor ya existe o viola una restricción de la base de datos";
        Map<String, String> errors = new HashMap<>();

        String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        if (rootMessage != null) {
            Pattern pattern = Pattern.compile("Detail:.*?\\((.+?)\\)=\\((.+?)\\)\\.", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(rootMessage);

            if (matcher.find()) {
                String field = matcher.group(1);
                String value = matcher.group(2);
                errors.put(field, "El valor '" + value + "' para el campo '" + field + "' ya existe.");
            } else {
                errors.put("error", defaultMessage);
            }
        } else {
            errors.put("error", defaultMessage);
        }

        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.CONFLICT.value(),
                "Violación de integridad de datos",
                null,
                errors
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Maneja excepciones no controladas en general
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAll(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error inesperado",
                null,
                Collections.singletonMap("error", ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Sobrescribe el manejo de HttpMessageNotReadableException (cuerpo mal formado o vacío)
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        log.error("Error al leer el cuerpo de la solicitud: {}", ex.getMessage(), ex);

        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Error en el formato del cuerpo de la solicitud",
                null,
                Collections.singletonMap("error", "El cuerpo de la solicitud está vacío o mal formado. Se esperaba JSON válido.")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Error de argumentos: {}", ex.getMessage());

        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                "Error de autenticación",
                null,
                Collections.singletonMap("error", ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldErrorResponse> errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String originalField = violation.getPropertyPath().toString();
                    String prefixedField = "roleData." + originalField;  // anteponer roleData.
                    return new FieldErrorResponse(prefixedField, List.of(violation.getMessage()));
                })
                .collect(Collectors.toList());

        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Errores en datos del rol",
                null,
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

}
