package com.example.demo.infrastructure.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateResourceRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,
        @Size(max = 500, message = "description must not exceed 500 characters")
        String description,
        @Size(max = 50, message = "status must not exceed 50 characters")
        String status
) {
}
