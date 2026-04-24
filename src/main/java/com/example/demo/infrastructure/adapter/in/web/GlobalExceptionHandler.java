package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.application.exception.ResourceNotFoundException;
import com.example.demo.domain.validation.DomainValidationException;
import com.example.demo.infrastructure.adapter.in.web.error.ApiError;
import com.example.demo.infrastructure.adapter.in.web.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainValidation(
            DomainValidationException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                new ApiError(
                        "DOMAIN_VALIDATION_ERROR",
                        ex.getMessage(),
                        null,
                        "$",
                        null
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleBeanValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiError(
                        "VALIDATION_ERROR",
                        fieldError.getDefaultMessage(),
                        attributeName(fieldError.getField()),
                        jsonPath(fieldError.getField()),
                        null
                ))
                .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        List<ApiError> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ApiError(
                        "VALIDATION_ERROR",
                        violation.getMessage(),
                        attributeName(violation.getPropertyPath().toString()),
                        jsonPath(violation.getPropertyPath().toString()),
                        null
                ))
                .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                new ApiError(
                        "INVALID_FORMAT",
                        "Invalid value '" + ex.getValue() + "'",
                        ex.getName(),
                        jsonPath(ex.getName()),
                        Map.of("rejectedValue", String.valueOf(ex.getValue()))
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                new ApiError(
                        "INVALID_FORMAT",
                        "Malformed or invalid request body",
                        null,
                        "$",
                        null
                )
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                new ApiError(
                        "DATA_INTEGRITY_VIOLATION",
                        "Data integrity constraint violated",
                        null,
                        "$",
                        null
                )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                new ApiError(
                        "RESOURCE_NOT_FOUND",
                        ex.getMessage(),
                        "resourceId",
                        "$.resourceId",
                        null
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedError(Exception ex, HttpServletRequest request) {
        LOGGER.error("Unhandled exception occurred while processing request {}", request.getRequestURI(), ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                new ApiError(
                        "INTERNAL_ERROR",
                        "Unexpected error",
                        null,
                        "$",
                        null
                )
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, ApiError error) {
        return buildResponse(status, List.of(error));
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, List<ApiError> errors) {
        ApiErrorResponse response = new ApiErrorResponse(errors);
        return ResponseEntity.status(status).body(response);
    }

    private String jsonPath(String attributePath) {
        if (attributePath == null || attributePath.isBlank()) {
            return "$";
        }
        if (attributePath.startsWith("$")) {
            return attributePath;
        }
        return "$." + attributePath;
    }

    private String attributeName(String attributePath) {
        if (attributePath == null || attributePath.isBlank()) {
            return null;
        }

        String withoutIndexes = attributePath.replaceAll("\\[[^]]*]", "");
        int lastDotIndex = withoutIndexes.lastIndexOf('.');
        if (lastDotIndex >= 0 && lastDotIndex < withoutIndexes.length() - 1) {
            return withoutIndexes.substring(lastDotIndex + 1);
        }
        return withoutIndexes;
    }
}
