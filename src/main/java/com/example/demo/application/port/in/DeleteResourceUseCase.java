package com.example.demo.application.port.in;

import java.util.UUID;

public interface DeleteResourceUseCase {

    void deleteResource(UUID resourceId);
}
