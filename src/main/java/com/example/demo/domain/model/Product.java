package com.example.demo.domain.model;

import com.example.demo.domain.validation.DomainValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public record Product(UUID id, String name, BigDecimal price) {

    public Product {
        if (id == null) {
            throw new DomainValidationException("Product id is required");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new DomainValidationException("Product name must not be blank");
        }
        name = name.trim();

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Product price must be greater than zero");
        }
        price = price.setScale(2, RoundingMode.HALF_UP);
    }
}
