package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.application.port.in.CreateResourceCommand;
import com.example.demo.application.port.in.CreateResourceUseCase;
import com.example.demo.application.port.in.GetResourceUseCase;
import com.example.demo.application.port.in.ListResourcesUseCase;
import com.example.demo.domain.model.Resource;
import com.example.demo.infrastructure.adapter.in.web.mapper.ResourceWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resources")
@Tag(name = "Resources", description = "Resource management APIs")
public class ResourceController {

    private final CreateResourceUseCase createResourceUseCase;
    private final ListResourcesUseCase listResourcesUseCase;
    private final GetResourceUseCase getResourceUseCase;
    private final ResourceWebMapper resourceWebMapper;

    public ResourceController(
            CreateResourceUseCase createResourceUseCase,
            ListResourcesUseCase listResourcesUseCase,
            GetResourceUseCase getResourceUseCase,
            ResourceWebMapper resourceWebMapper
    ) {
        this.createResourceUseCase = createResourceUseCase;
        this.listResourcesUseCase = listResourcesUseCase;
        this.getResourceUseCase = getResourceUseCase;
        this.resourceWebMapper = resourceWebMapper;
    }

    @PostMapping
    @Operation(summary = "Create resource", description = "Creates a new resource")
    public ResponseEntity<ResourceResponse> createResource(@Valid @RequestBody CreateResourceRequest request) {
        CreateResourceCommand command = resourceWebMapper.toCreateCommand(request);
        Resource createdResource = createResourceUseCase.createResource(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceWebMapper.toResponse(createdResource));
    }

    @GetMapping
    @Operation(summary = "List resources", description = "Returns all resources")
    public List<ResourceResponse> listResources() {
        return resourceWebMapper.toResponseList(listResourcesUseCase.listResources());
    }

    @GetMapping("/{resourceId}")
    @Operation(summary = "Get resource", description = "Returns a resource by id")
    public ResourceResponse getResource(@PathVariable UUID resourceId) {
        return resourceWebMapper.toResponse(getResourceUseCase.getResource(resourceId));
    }
}
