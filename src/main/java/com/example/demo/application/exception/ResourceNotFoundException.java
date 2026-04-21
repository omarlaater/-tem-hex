package com.example.demo.application.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(UUID resourceId) {
        super("Resource not found: " + resourceId);
    }
}
