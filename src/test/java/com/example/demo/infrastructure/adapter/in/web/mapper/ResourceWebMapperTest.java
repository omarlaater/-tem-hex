package com.example.demo.infrastructure.adapter.in.web.mapper;

import com.example.demo.application.port.in.CreateResourceCommand;
import com.example.demo.domain.model.Resource;
import com.example.demo.infrastructure.adapter.in.web.CreateResourceRequest;
import com.example.demo.infrastructure.adapter.in.web.ResourceResponse;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceWebMapperTest {

    private final ResourceWebMapper mapper = new ResourceWebMapper();

    @Test
    void shouldMapRequestToCommand() {
        CreateResourceRequest request = new CreateResourceRequest("Laptop", "Portable", "ACTIVE");

        CreateResourceCommand command = mapper.toCreateCommand(request);

        assertEquals("Laptop", command.name());
        assertEquals("Portable", command.description());
        assertEquals("ACTIVE", command.status());
    }

    @Test
    void shouldMapDomainListToResponseList() {
        Resource resource = new Resource(UUID.randomUUID(), "Mouse", null, null);

        List<ResourceResponse> responses = mapper.toResponseList(List.of(resource));

        assertEquals(1, responses.size());
        assertEquals("Mouse", responses.get(0).name());
        assertEquals(Resource.DEFAULT_STATUS, responses.get(0).status());
    }
}
