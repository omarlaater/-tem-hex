package com.example.demo.infrastructure.adapter.out.persistence;

import com.example.demo.application.port.out.ResourceRepositoryPort;
import com.example.demo.domain.model.Resource;
import com.example.demo.infrastructure.adapter.out.persistence.mapper.ResourcePersistenceMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JpaResourceRepositoryAdapter implements ResourceRepositoryPort {

    private final SpringDataResourceRepository springDataResourceRepository;
    private final ResourcePersistenceMapper resourcePersistenceMapper;

    public JpaResourceRepositoryAdapter(
            SpringDataResourceRepository springDataResourceRepository,
            ResourcePersistenceMapper resourcePersistenceMapper
    ) {
        this.springDataResourceRepository = springDataResourceRepository;
        this.resourcePersistenceMapper = resourcePersistenceMapper;
    }

    @Override
    public Resource save(Resource resource) {
        ResourceJpaEntity entity = resourcePersistenceMapper.toEntity(resource);
        ResourceJpaEntity savedEntity = springDataResourceRepository.save(entity);
        return resourcePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public List<Resource> findAll() {
        return resourcePersistenceMapper.toDomainList(springDataResourceRepository.findAll());
    }

    @Override
    public Optional<Resource> findById(UUID resourceId) {
        return springDataResourceRepository.findById(resourceId)
                .map(resourcePersistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID resourceId) {
        return springDataResourceRepository.existsById(resourceId);
    }

    @Override
    public void deleteById(UUID resourceId) {
        springDataResourceRepository.deleteById(resourceId);
    }
}
