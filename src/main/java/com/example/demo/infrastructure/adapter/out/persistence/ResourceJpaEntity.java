package com.example.demo.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "resources")
public class ResourceJpaEntity {

    @Id
    @Column(name = "resource_id", nullable = false)
    private UUID resourceId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String status;

    protected ResourceJpaEntity() {
        // JPA
    }

    public ResourceJpaEntity(UUID resourceId, String name, String description, String status) {
        this.resourceId = resourceId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
