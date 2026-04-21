package com.example.demo.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "ResourceResponse", description = "Resource returned by the API")
public record ResourceResponse(
        @Schema(
                description = "Unique identifier of the resource",
                example = "f78a8762-f661-45c8-b45a-1d154a61f0a5",
                format = "uuid"
        )
        UUID resourceId,
        @Schema(description = "Name of the resource", example = "Monitor", maxLength = 100)
        String name,
        @Schema(description = "Optional resource description", example = "External display", maxLength = 500)
        String description,
        @Schema(description = "Current resource status", example = "ACTIVE", maxLength = 50)
        String status
) {
}
