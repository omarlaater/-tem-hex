package com.example.demo.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataResourceRepository extends JpaRepository<ResourceJpaEntity, UUID> {
}
