package com.example.demo.application.port.in;

import com.example.demo.domain.model.Product;

public interface CreateProductUseCase {

    Product createProduct(CreateProductCommand command);
}
