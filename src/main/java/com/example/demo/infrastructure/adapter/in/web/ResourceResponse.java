package com.example.demo.infrastructure.adapter.in.web;

import java.util.UUID;

public record ResourceResponse(UUID resourceId, String name, String description, String status) {
}
