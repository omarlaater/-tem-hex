package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.infrastructure.adapter.out.persistence.SpringDataResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ResourceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataResourceRepository springDataResourceRepository;

    @BeforeEach
    void cleanDatabase() {
        springDataResourceRepository.deleteAll();
    }

    @Test
    void shouldCreateGetAndListResources() throws Exception {
        String response = mockMvc.perform(post("/api/v1/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Monitor",
                                  "description": "External display"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resourceId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Monitor"))
                .andExpect(jsonPath("$.description").value("External display"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String resourceId = response.replaceAll(".*\"resourceId\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/api/v1/resources/{resourceId}", resourceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceId").value(resourceId))
                .andExpect(jsonPath("$.name").value("Monitor"));

        mockMvc.perform(get("/api/v1/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].resourceId").value(matchesPattern(".+")))
                .andExpect(jsonPath("$[0].name").value("Monitor"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void shouldReturnStructuredValidationErrorForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": ""
                                }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[0].message").value("name is required"))
                .andExpect(jsonPath("$.errors[0].attribute").value("name"))
                .andExpect(jsonPath("$.errors[0].path").value("$.name"));
    }

    @Test
    void shouldReturnStructuredNotFoundError() throws Exception {
        mockMvc.perform(get("/api/v1/resources/{resourceId}", "f78a8762-f661-45c8-b45a-1d154a61f0a5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.errors[0].attribute").value("resourceId"))
                .andExpect(jsonPath("$.errors[0].path").value("$.resourceId"));
    }

    @Test
    void shouldExposeDocumentedOpenApiContract() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.type").value("http"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.bearerFormat").value("JWT"))
                .andExpect(jsonPath("$.paths['/api/v1/resources'].post.operationId").value("createResource"))
                .andExpect(jsonPath("$.paths['/api/v1/resources'].post.responses['201'].description")
                        .value("Resource created successfully"))
                .andExpect(jsonPath("$.paths['/api/v1/resources'].post.responses['400'].description")
                        .value("Invalid request payload"))
                .andExpect(jsonPath("$.paths['/api/v1/resources'].post.responses['401'].description")
                        .value("Unauthorized - missing or invalid authentication token"))
                .andExpect(jsonPath("$.paths['/api/v1/resources'].post.responses['403'].description")
                        .value("Forbidden - insufficient permissions to create resource"))
                .andExpect(jsonPath("$.paths['/api/v1/resources'].get.operationId").value("listResources"))
                .andExpect(jsonPath("$.paths['/api/v1/resources/{resourceId}'].get.operationId").value("getResource"))
                .andExpect(jsonPath("$.paths['/api/v1/resources/{resourceId}'].get.responses['404'].description")
                        .value("Resource not found"))
                .andExpect(jsonPath("$.components.schemas.CreateResourceRequest.properties.name.maxLength").value(100))
                .andExpect(jsonPath("$.components.schemas.ResourceResponse.properties.resourceId.format").value("uuid"))
                .andExpect(jsonPath("$.components.schemas.ApiErrorResponse.properties.errors.description")
                        .value("List of API errors"))
                .andExpect(jsonPath("$.components.schemas.ApiError.properties.path.example")
                        .value("$.addresses[2].zip"));
    }
}
