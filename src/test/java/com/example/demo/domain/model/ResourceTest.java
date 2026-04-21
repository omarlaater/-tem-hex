package com.example.demo.domain.model;

import com.example.demo.domain.validation.DomainValidationException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceTest {

    @Test
    void shouldCreateValidResource() {
        Resource resource = new Resource(UUID.randomUUID(), " Document ", " Notes ", " DRAFT ");

        assertEquals("Document", resource.name());
        assertEquals("Notes", resource.description());
        assertEquals("DRAFT", resource.status());
    }

    @Test
    void shouldDefaultStatusToActive() {
        Resource resource = new Resource(UUID.randomUUID(), "Document", null, null);

        assertNull(resource.description());
        assertEquals(Resource.DEFAULT_STATUS, resource.status());
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThrows(
                DomainValidationException.class,
                () -> new Resource(UUID.randomUUID(), "   ", null, null)
        );
    }

    @Test
    void shouldFailWhenNameIsTooLong() {
        String name = "a".repeat(101);

        assertThrows(
                DomainValidationException.class,
                () -> new Resource(UUID.randomUUID(), name, null, null)
        );
    }
}
