package com.example.demo.infrastructure.adapter.out.persistence.mapper;

import com.example.demo.domain.model.Resource;
import com.example.demo.infrastructure.adapter.out.persistence.ResourceJpaEntity;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourcePersistenceMapperTest {

    private final ResourcePersistenceMapper mapper = new ResourcePersistenceMapper();

    @Test
    void shouldMapDomainToEntityAndBack() {
        UUID resourceId = UUID.randomUUID();
        Resource resource = new Resource(resourceId, "Keyboard", "Mechanical", "ACTIVE");

        ResourceJpaEntity entity = mapper.toEntity(resource);
        Resource mappedBack = mapper.toDomain(entity);

        assertEquals(resourceId, entity.getResourceId());
        assertEquals("Keyboard", entity.getName());
        assertEquals("Mechanical", entity.getDescription());
        assertEquals("ACTIVE", entity.getStatus());

        assertEquals(resourceId, mappedBack.resourceId());
        assertEquals("Keyboard", mappedBack.name());
        assertEquals("Mechanical", mappedBack.description());
        assertEquals("ACTIVE", mappedBack.status());
    }
}
