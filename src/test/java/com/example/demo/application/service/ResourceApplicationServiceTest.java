package com.example.demo.application.service;

import com.example.demo.application.exception.ResourceNotFoundException;
import com.example.demo.application.port.in.CreateResourceCommand;
import com.example.demo.application.port.in.UpdateResourceCommand;
import com.example.demo.application.port.out.ResourceRepositoryPort;
import com.example.demo.domain.model.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceApplicationServiceTest {

    @Mock
    private ResourceRepositoryPort resourceRepositoryPort;

    @InjectMocks
    private ResourceApplicationService resourceApplicationService;

    @Test
    void shouldCreateResource() {
        when(resourceRepositoryPort.save(any(Resource.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Resource created = resourceApplicationService.createResource(
                new CreateResourceCommand("Keyboard", "Mechanical keyboard", null)
        );

        assertNotNull(created.resourceId());
        assertEquals("Keyboard", created.name());
        assertEquals("Mechanical keyboard", created.description());
        assertEquals(Resource.DEFAULT_STATUS, created.status());
    }

    @Test
    void shouldListResources() {
        Resource resource = new Resource(UUID.randomUUID(), "Mouse", null, "ACTIVE");
        when(resourceRepositoryPort.findAll()).thenReturn(List.of(resource));

        List<Resource> resources = resourceApplicationService.listResources();

        assertEquals(1, resources.size());
        assertEquals("Mouse", resources.get(0).name());
    }

    @Test
    void shouldGetResourceById() {
        UUID resourceId = UUID.randomUUID();
        Resource resource = new Resource(resourceId, "Mouse", null, "ACTIVE");
        when(resourceRepositoryPort.findById(resourceId)).thenReturn(Optional.of(resource));

        Resource found = resourceApplicationService.getResource(resourceId);

        assertEquals(resourceId, found.resourceId());
    }

    @Test
    void shouldFailWhenResourceIsMissing() {
        UUID resourceId = UUID.randomUUID();
        when(resourceRepositoryPort.findById(resourceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceApplicationService.getResource(resourceId));
    }

    @Test
    void shouldUpdateResource() {
        UUID resourceId = UUID.randomUUID();
        when(resourceRepositoryPort.existsById(resourceId)).thenReturn(true);
        when(resourceRepositoryPort.save(any(Resource.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Resource updated = resourceApplicationService.updateResource(
                new UpdateResourceCommand(resourceId, "Updated", "Updated description", "INACTIVE")
        );

        assertEquals(resourceId, updated.resourceId());
        assertEquals("Updated", updated.name());
        assertEquals("Updated description", updated.description());
        assertEquals("INACTIVE", updated.status());
    }

    @Test
    void shouldDeleteResource() {
        UUID resourceId = UUID.randomUUID();
        when(resourceRepositoryPort.existsById(resourceId)).thenReturn(true);

        resourceApplicationService.deleteResource(resourceId);

        verify(resourceRepositoryPort).deleteById(resourceId);
    }
}
