package com.example.demo.infrastructure.adapter.in.web.mapper;

import com.example.demo.application.port.in.CreateResourceCommand;
import com.example.demo.application.port.in.UpdateResourceCommand;
import com.example.demo.domain.model.Resource;
import com.example.demo.infrastructure.adapter.in.web.CreateResourceRequest;
import com.example.demo.infrastructure.adapter.in.web.ResourceResponse;
import com.example.demo.infrastructure.adapter.in.web.UpdateResourceRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ResourceWebMapper {

    public CreateResourceCommand toCreateCommand(CreateResourceRequest request) {
        return new CreateResourceCommand(request.name(), request.description(), request.status());
    }

    public UpdateResourceCommand toUpdateCommand(UUID resourceId, UpdateResourceRequest request) {
        return new UpdateResourceCommand(resourceId, request.name(), request.description(), request.status());
    }

    public ResourceResponse toResponse(Resource resource) {
        return new ResourceResponse(
                resource.resourceId(),
                resource.name(),
                resource.description(),
                resource.status()
        );
    }

    public List<ResourceResponse> toResponseList(List<Resource> resources) {
        return resources.stream()
                .map(this::toResponse)
                .toList();
    }
}
