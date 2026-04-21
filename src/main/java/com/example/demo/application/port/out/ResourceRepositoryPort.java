package com.example.demo.application.port.out;

import com.example.demo.domain.model.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResourceRepositoryPort {

    Resource save(Resource resource);

    List<Resource> findAll();

    Optional<Resource> findById(UUID resourceId);

    boolean existsById(UUID resourceId);

    void deleteById(UUID resourceId);
}
