package com.lbg.alert.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ── 400: Bean Validation ──────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<Violation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new Violation(fe.getField(),
                        fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value"))
                .toList();

        log.warn("Validation failed on {}: {}", request.getRequestURI(), violations);
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(HttpStatus.BAD_REQUEST, "Validation failed",
                        request.getRequestURI(), violations));
    }

    // ── 400: Invalid groupBy parameter ───────────────────────
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Bad request on {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.badRequest().body(
                ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage(),
                        request.getRequestURI(), List.of()));
    }

    // ── 404: Resource not found ───────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Not found on {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage(),
                        request.getRequestURI(), List.of()));
    }

    // ── 422: Business rule violation ─────────────────────────
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(
            BusinessRuleException ex, HttpServletRequest request) {
        log.warn("Business rule violation on {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.unprocessableEntity().body(
                ErrorResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(),
                        request.getRequestURI(), List.of()));
    }

    // ── 500: Catch-all ────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception on {}", request.getRequestURI(), ex);
        return ResponseEntity.internalServerError().body(
                ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred",
                        request.getRequestURI(), List.of()));
    }

    // ── Error response body ───────────────────────────────────
    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private Instant timestamp;
        private int status;
        private String error;
        private String path;
        private List<Violation> violations;

        static ErrorResponse of(HttpStatus httpStatus, String error,
                                String path, List<Violation> violations) {
            return new ErrorResponse(Instant.now(), httpStatus.value(),
                    error, path, violations);
        }
    }

    @Getter @AllArgsConstructor
    public static class Violation {
        private String field;
        private String message;
    }
}