package com.example.demo.infrastructure.adapter.in.web.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiValidationError", description = "Detailed field validation error")
public record ApiValidationError(
        @Schema(description = "Field or attribute that caused the error", example = "name")
        String field,
        @Schema(description = "Detailed validation error message", example = "name is required")
        String message
) {
}
