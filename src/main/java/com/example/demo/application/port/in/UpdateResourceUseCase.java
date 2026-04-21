package com.example.demo.application.port.in;

import com.example.demo.domain.model.Resource;

public interface UpdateResourceUseCase {

    Resource updateResource(UpdateResourceCommand command);
}
