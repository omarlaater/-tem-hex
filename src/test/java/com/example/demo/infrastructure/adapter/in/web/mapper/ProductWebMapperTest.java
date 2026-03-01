package com.example.demo.infrastructure.adapter.in.web.mapper;

import com.example.demo.application.port.in.CreateProductCommand;
import com.example.demo.domain.model.Product;
import com.example.demo.infrastructure.adapter.in.web.CreateProductRequest;
import com.example.demo.infrastructure.adapter.in.web.ProductResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductWebMapperTest {

    private final ProductWebMapper mapper = new ProductWebMapper();

    @Test
    void shouldMapRequestToCommand() {
        CreateProductRequest request = new CreateProductRequest("Laptop", new BigDecimal("1500.50"));

        CreateProductCommand command = mapper.toCreateCommand(request);

        assertEquals("Laptop", command.name());
        assertEquals(new BigDecimal("1500.50"), command.price());
    }

    @Test
    void shouldMapDomainListToResponseList() {
        Product product = new Product(UUID.randomUUID(), "Mouse", new BigDecimal("29.99"));

        List<ProductResponse> responses = mapper.toResponseList(List.of(product));

        assertEquals(1, responses.size());
        assertEquals("Mouse", responses.get(0).name());
        assertEquals(new BigDecimal("29.99"), responses.get(0).price());
    }
}
