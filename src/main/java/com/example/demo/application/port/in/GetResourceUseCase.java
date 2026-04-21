package com.example.demo.application.port.in;

import com.example.demo.domain.model.Resource;
import java.util.UUID;

public interface GetResourceUseCase {

    Resource getResource(UUID resourceId);
}
