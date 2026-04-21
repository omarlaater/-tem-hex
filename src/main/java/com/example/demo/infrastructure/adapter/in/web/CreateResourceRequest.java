package com.example.demo.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateResourceRequest", description = "Payload used to create a resource")
public record CreateResourceRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must not exceed 100 characters")
        @Schema(
                description = "Name of the resource",
                example = "Monitor",
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name,
        @Size(max = 500, message = "description must not exceed 500 characters")
        @Schema(
                description = "Optional resource description",
                example = "External display",
                maxLength = 500,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String description,
        @Size(max = 50, message = "status must not exceed 50 characters")
        @Schema(
                description = "Optional resource status. Defaults to ACTIVE when omitted.",
                example = "ACTIVE",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String status
) {
}
