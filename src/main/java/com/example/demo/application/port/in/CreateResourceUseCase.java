package com.example.demo.application.port.in;

import com.example.demo.domain.model.Resource;

public interface CreateResourceUseCase {

    Resource createResource(CreateResourceCommand command);
}
