package com.example.demo.domain.model;

import com.example.demo.domain.validation.DomainValidationException;
import java.util.UUID;

public record Resource(UUID resourceId, String name, String description, String status) {

    public static final String DEFAULT_STATUS = "ACTIVE";
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final int MAX_STATUS_LENGTH = 50;

    public Resource {
        if (resourceId == null) {
            throw new DomainValidationException("Resource id is required");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new DomainValidationException("Resource name is required");
        }
        name = name.trim();
        if (name.length() > MAX_NAME_LENGTH) {
            throw new DomainValidationException("Resource name must not exceed 100 characters");
        }

        if (description != null) {
            description = description.trim();
            if (description.length() > MAX_DESCRIPTION_LENGTH) {
                throw new DomainValidationException("Resource description must not exceed 500 characters");
            }
        }

        if (status == null || status.trim().isEmpty()) {
            status = DEFAULT_STATUS;
        } else {
            status = status.trim();
        }
        if (status.length() > MAX_STATUS_LENGTH) {
            throw new DomainValidationException("Resource status must not exceed 50 characters");
        }
    }
}
