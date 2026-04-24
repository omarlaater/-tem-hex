package com.example.demo.infrastructure.adapter.in.web.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "ApiErrorResponse", description = "Standard API error response")
public record ApiErrorResponse(
        @Schema(description = "List of API errors")
        List<ApiError> errors
) {
}
