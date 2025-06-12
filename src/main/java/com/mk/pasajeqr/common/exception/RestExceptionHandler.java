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
    /**
     * ⚙️ SPRING - Body malformado o vacío (parseo JSON fallido).
     * Ocurre cuando el cuerpo del request no puede ser leído o convertido al DTO esperado.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        log.error("❌ Body de solicitud ilegible o vacío (parseo): {}", ex.getMessage(), ex);

        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Error en el formato del cuerpo de la solicitud",
                null,
                Collections.singletonMap("error", "El cuerpo de la solicitud está vacío o mal formado. Se esperaba JSON válido.")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
    }

    /**
     * ⚙️ SPRING - Errores de validación automática (@Valid) en DTO's.
     * Se activa cuando un campo requerido es nulo, formato inválido, tamaño fuera de rango, etc.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        log.warn("❌ Validación automática fallida (@Valid): {}", ex.getMessage());

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

    /**
     * ⚙️ SPRING - Errores de integridad referencial en persistencia (JPA/Hibernate).
     * Se activa ante violaciones como claves duplicadas, nulls prohibidos o restricciones únicas.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("❌ Violación de integridad de datos (JPA/Hibernate): {}", ex.getMessage(), ex);

        String defaultMessage = "El valor ya existe o viola una restricción de la base de datos";
        Map<String, String> errors = new HashMap<>();

        String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        if (rootMessage != null) {
            // Caso 1: Violación de restricción única (duplicate key)
            Pattern duplicatePattern = Pattern.compile("Detail:.*?\\((.+?)\\)=\\((.+?)\\)\\.", Pattern.CASE_INSENSITIVE);
            Matcher duplicateMatcher = duplicatePattern.matcher(rootMessage);

            if (duplicateMatcher.find()) {
                String field = duplicateMatcher.group(1);
                String value = duplicateMatcher.group(2);
                errors.put(field, "El valor '" + value + "' para el campo '" + field + "' ya existe.");
            }
            // Caso 2: Violación de integridad referencial (foreign key constraint)
            else if (rootMessage.toLowerCase().contains("foreign key constraint fails") ||
                    rootMessage.toLowerCase().contains("violates foreign key constraint")) {
                errors.put("error", "No se puede eliminar el recurso porque está siendo usado en otra parte del sistema");
            }
            // Otro tipo de error de integridad
            else {
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

    /**
     * ⚙️ SPRING - Fallback para errores no controlados (Exception genérica).
     * Captura excepciones no manejadas previamente y evita errores 500 sin formato.
     */
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

    /**
     * ⚙️ Maneja violaciones de restricción generales (ConstraintViolationException)
     * que pueden ocurrir en cualquier parte del request o parámetros.
     * Devuelve un 400 BAD REQUEST con detalles estándar de validación sin prefijos.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleGenericConstraintViolation(ConstraintViolationException ex) {
        List<FieldErrorResponse> errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String fullPath = violation.getPropertyPath().toString(); // ej. "getDriverById.id"
                    String[] parts = fullPath.split("\\.");
                    String field = parts[parts.length - 1]; // "id"

                    return new FieldErrorResponse(field, List.of(violation.getMessage()));
                })
                .toList();

        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Errores de validación",
                null,
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * ⚙️ Maneja violaciones de restricción específicas de la validación manual
     * del objeto roleData, anteponiendo "roleData." a los campos en el mensaje.
     * Retorna un 400 BAD REQUEST con errores detallados y prefijados para claridad.
     */
    @ExceptionHandler(RoleDataConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleRoleDataConstraintViolation(RoleDataConstraintViolationException ex) {
        List<FieldErrorResponse> errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String originalField = violation.getPropertyPath().toString();
                    String prefixedField = "roleData." + originalField;
                    return new FieldErrorResponse(prefixedField, List.of(violation.getMessage()));
                })
                .toList();

        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Errores en datos del rol",
                null,
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 🧑‍💻 MANUAL - El recurso ya existe (clave duplicada verificada en código antes del insert).
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicate(DuplicateResourceException ex) {
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.CONFLICT.value(),
                "Recurso duplicado",
                null,
                Collections.singletonMap("error", ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * 🧑‍💻 MANUAL - Cuando no se encuentra un recurso solicitado (por ID, email, etc.).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                null,
                Collections.singletonMap("error", ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 🧑‍💻 MANUAL - Cuando el usuario no está autenticado o token inválido.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException ex) {
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                "No autorizado",
                null,
                Collections.singletonMap("error", ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 🧑‍💻 MANUAL - Usuario autenticado pero sin permisos suficientes.
     */
    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ApiResponse<?>> handleForbidden(ForbiddenActionException ex) {
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.FORBIDDEN.value(),
                "Acción no permitida",
                null,
                Collections.singletonMap("error", ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 🧑‍💻 MANUAL - Lógica de negocio lanza esta excepción cuando hay parámetros inválidos (no a nivel de @Valid).
     * Ej.: fecha inicio > fecha fin, combinación de datos ilógica, etc.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex) {
        log.warn("⚠️ Solicitud inválida (lógica negocio): {}", ex.getMessage());
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud inválida",
                null,
                Collections.singletonMap("error", ex.getMessage())
        );
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 🧑‍💻 MANUAL - Validación personalizada no cubierta por @Valid ni ConstraintViolation.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomValidation(ValidationException ex) {
        log.warn("⚠️ Validación manual fallida: {}", ex.getMessage());
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                null,
                Collections.singletonMap("error", ex.getMessage())
        );
        return ResponseEntity.badRequest().body(response);
    }
}
