package com.example.demo.infrastructure.adapter.out.persistence.mapper;

import com.example.demo.domain.model.Resource;
import com.example.demo.infrastructure.adapter.out.persistence.ResourceJpaEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResourcePersistenceMapper {

    public ResourceJpaEntity toEntity(Resource resource) {
        return new ResourceJpaEntity(
                resource.resourceId(),
                resource.name(),
                resource.description(),
                resource.status()
        );
    }

    public Resource toDomain(ResourceJpaEntity entity) {
        return new Resource(
                entity.getResourceId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStatus()
        );
    }

    public List<Resource> toDomainList(List<ResourceJpaEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
