package com.example.demo.domain.model;

import com.example.demo.domain.validation.DomainValidationException;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void shouldCreateValidProduct() {
        Product product = new Product(UUID.randomUUID(), "Laptop", new BigDecimal("1200"));

        assertEquals("Laptop", product.name());
        assertEquals(new BigDecimal("1200.00"), product.price());
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThrows(
                DomainValidationException.class,
                () -> new Product(UUID.randomUUID(), "   ", new BigDecimal("1200"))
        );
    }

    @Test
    void shouldFailWhenPriceIsNotPositive() {
        assertThrows(
                DomainValidationException.class,
                () -> new Product(UUID.randomUUID(), "Laptop", BigDecimal.ZERO)
        );
    }
}
