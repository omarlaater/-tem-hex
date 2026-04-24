package com.example.demo.infrastructure.adapter.in.web.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ApiError", description = "Single API error")
public record ApiError(
        @Schema(description = "Application error code", example = "INVALID_FORMAT", maxLength = 50)
        String code,
        @Schema(description = "Human-readable error message", example = "Invalid format", maxLength = 500)
        String message,
        @Schema(description = "Attribute that caused the error", example = "zip", maxLength = 100)
        String attribute,
        @Schema(description = "JSON path of the invalid attribute", example = "$.addresses[2].zip", maxLength = 200)
        String path,
        @Schema(description = "Additional error information")
        Map<String, Object> additionalInformation
) {
}
