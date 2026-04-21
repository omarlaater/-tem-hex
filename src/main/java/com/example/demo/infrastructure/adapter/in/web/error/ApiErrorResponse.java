package com.example.demo.infrastructure.adapter.in.web.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@Schema(name = "ApiErrorResponse", description = "Standard API error response")
public record ApiErrorResponse(
        @Schema(description = "Timestamp when the error occurred", example = "2026-03-02T00:00:00Z")
        Instant timestamp,
        @Schema(description = "HTTP status code", example = "400", minimum = "100", maximum = "599")
        int status,
        @Schema(description = "HTTP reason phrase", example = "Bad Request")
        String error,
        @Schema(description = "Application-specific error code", example = "VALIDATION_ERROR")
        String code,
        @Schema(description = "Human-readable error message", example = "Request validation failed")
        String message,
        @Schema(description = "Request path that caused the error", example = "/api/v1/resources")
        String path,
        @Schema(description = "List of detailed validation errors")
        List<ApiValidationError> details
) {
}
