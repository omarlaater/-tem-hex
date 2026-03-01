package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.domain.model.Product;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID id, String name, BigDecimal price) {

    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(product.id(), product.name(), product.price());
    }
}
