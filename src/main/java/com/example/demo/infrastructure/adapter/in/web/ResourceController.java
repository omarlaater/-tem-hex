package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.application.port.in.CreateResourceCommand;
import com.example.demo.application.port.in.CreateResourceUseCase;
import com.example.demo.application.port.in.DeleteResourceUseCase;
import com.example.demo.application.port.in.GetResourceUseCase;
import com.example.demo.application.port.in.ListResourcesUseCase;
import com.example.demo.application.port.in.UpdateResourceCommand;
import com.example.demo.application.port.in.UpdateResourceUseCase;
import com.example.demo.domain.model.Resource;
import com.example.demo.infrastructure.adapter.in.web.error.ApiErrorResponse;
import com.example.demo.infrastructure.adapter.in.web.mapper.ResourceWebMapper;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resources")
@Tag(name = "Resources", description = "Resource management APIs")
@SecurityRequirement(name = "bearerAuth")
public class ResourceController {

    private final CreateResourceUseCase createResourceUseCase;
    private final ListResourcesUseCase listResourcesUseCase;
    private final GetResourceUseCase getResourceUseCase;
    private final UpdateResourceUseCase updateResourceUseCase;
    private final DeleteResourceUseCase deleteResourceUseCase;
    private final ResourceWebMapper resourceWebMapper;

    public ResourceController(
            CreateResourceUseCase createResourceUseCase,
            ListResourcesUseCase listResourcesUseCase,
            GetResourceUseCase getResourceUseCase,
            UpdateResourceUseCase updateResourceUseCase,
            DeleteResourceUseCase deleteResourceUseCase,
            ResourceWebMapper resourceWebMapper
    ) {
        this.createResourceUseCase = createResourceUseCase;
        this.listResourcesUseCase = listResourcesUseCase;
        this.getResourceUseCase = getResourceUseCase;
        this.updateResourceUseCase = updateResourceUseCase;
        this.deleteResourceUseCase = deleteResourceUseCase;
        this.resourceWebMapper = resourceWebMapper;
    }

    @PostMapping
    @Operation(
            summary = "Create resource",
            description = "Creates a new resource with name, description, and status.",
            operationId = "createResource"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Resource created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid authentication token",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions to create resource",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<ResourceResponse> createResource(@Valid @RequestBody CreateResourceRequest request) {
        CreateResourceCommand command = resourceWebMapper.toCreateCommand(request);
        Resource createdResource = createResourceUseCase.createResource(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceWebMapper.toResponse(createdResource));
    }

    @GetMapping
    @Operation(
            summary = "List all resources",
            description = "Retrieves a list of all available resources in the system.",
            operationId = "listResources"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of resources returned successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ResourceResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid authentication token",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions to view resources",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    public List<ResourceResponse> listResources() {
        return resourceWebMapper.toResponseList(listResourcesUseCase.listResources());
    }

    @GetMapping("/{resourceId}")
    @Operation(
            summary = "Get resource by id",
            description = "Retrieves a single resource by its unique identifier.",
            operationId = "getResource"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resource returned successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid resource identifier",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid authentication token",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions to view resource",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    public ResourceResponse getResource(
            @Parameter(description = "Unique resource identifier", example = "f78a8762-f661-45c8-b45a-1d154a61f0a5")
            @PathVariable UUID resourceId
    ) {
        return resourceWebMapper.toResponse(getResourceUseCase.getResource(resourceId));
    }

    @PutMapping("/{resourceId}")
    @Operation(
            summary = "Update resource",
            description = "Updates an existing resource identified by its unique identifier.",
            operationId = "updateResource"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resource updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResourceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid authentication token",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions to update resource",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    public ResourceResponse updateResource(
            @Parameter(description = "Unique resource identifier", example = "f78a8762-f661-45c8-b45a-1d154a61f0a5")
            @PathVariable UUID resourceId,
            @Valid @RequestBody UpdateResourceRequest request
    ) {
        UpdateResourceCommand command = resourceWebMapper.toUpdateCommand(resourceId, request);
        Resource updatedResource = updateResourceUseCase.updateResource(command);
        return resourceWebMapper.toResponse(updatedResource);
    }

    @DeleteMapping("/{resourceId}")
    @Operation(
            summary = "Delete resource",
            description = "Deletes a resource identified by its unique identifier.",
            operationId = "deleteResource"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Resource deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid resource identifier",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid authentication token",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions to delete resource",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<Void> deleteResource(
            @Parameter(description = "Unique resource identifier", example = "f78a8762-f661-45c8-b45a-1d154a61f0a5")
            @PathVariable UUID resourceId
    ) {
        deleteResourceUseCase.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }
}
