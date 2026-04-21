package com.example.demo.application.service;

import com.example.demo.application.exception.ResourceNotFoundException;
import com.example.demo.application.port.in.CreateResourceCommand;
import com.example.demo.application.port.in.CreateResourceUseCase;
import com.example.demo.application.port.in.DeleteResourceUseCase;
import com.example.demo.application.port.in.GetResourceUseCase;
import com.example.demo.application.port.in.ListResourcesUseCase;
import com.example.demo.application.port.in.UpdateResourceCommand;
import com.example.demo.application.port.in.UpdateResourceUseCase;
import com.example.demo.application.port.out.ResourceRepositoryPort;
import com.example.demo.domain.model.Resource;
import com.example.demo.domain.validation.DomainValidationException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ResourceApplicationService implements
        CreateResourceUseCase,
        ListResourcesUseCase,
        GetResourceUseCase,
        UpdateResourceUseCase,
        DeleteResourceUseCase {

    private final ResourceRepositoryPort resourceRepositoryPort;

    public ResourceApplicationService(ResourceRepositoryPort resourceRepositoryPort) {
        this.resourceRepositoryPort = resourceRepositoryPort;
    }

    @Override
    @Transactional
    public Resource createResource(CreateResourceCommand command) {
        Resource resource = new Resource(
                UUID.randomUUID(),
                command.name(),
                command.description(),
                command.status()
        );
        return resourceRepositoryPort.save(resource);
    }

    @Override
    public List<Resource> listResources() {
        return resourceRepositoryPort.findAll();
    }

    @Override
    public Resource getResource(UUID resourceId) {
        validateResourceId(resourceId);
        return resourceRepositoryPort.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException(resourceId));
    }

    @Override
    @Transactional
    public Resource updateResource(UpdateResourceCommand command) {
        validateResourceId(command.resourceId());
        if (!resourceRepositoryPort.existsById(command.resourceId())) {
            throw new ResourceNotFoundException(command.resourceId());
        }

        Resource resource = new Resource(
                command.resourceId(),
                command.name(),
                command.description(),
                command.status()
        );
        return resourceRepositoryPort.save(resource);
    }

    @Override
    @Transactional
    public void deleteResource(UUID resourceId) {
        validateResourceId(resourceId);
        if (!resourceRepositoryPort.existsById(resourceId)) {
            throw new ResourceNotFoundException(resourceId);
        }
        resourceRepositoryPort.deleteById(resourceId);
    }

    private void validateResourceId(UUID resourceId) {
        if (resourceId == null) {
            throw new DomainValidationException("Resource id is required");
        }
    }
}
