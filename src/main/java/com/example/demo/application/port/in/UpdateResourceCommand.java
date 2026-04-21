package com.example.demo.application.port.in;

import java.util.UUID;

public record UpdateResourceCommand(UUID resourceId, String name, String description, String status) {
}
