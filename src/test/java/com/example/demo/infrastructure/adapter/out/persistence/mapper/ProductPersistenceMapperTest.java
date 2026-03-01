package com.example.demo.infrastructure.adapter.out.persistence.mapper;

import com.example.demo.domain.model.Product;
import com.example.demo.infrastructure.adapter.out.persistence.ProductJpaEntity;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductPersistenceMapperTest {

    private final ProductPersistenceMapper mapper = new ProductPersistenceMapper();

    @Test
    void shouldMapDomainToEntityAndBack() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Keyboard", new BigDecimal("89.90"));

        ProductJpaEntity entity = mapper.toEntity(product);
        Product mappedBack = mapper.toDomain(entity);

        assertEquals(productId, entity.getId());
        assertEquals("Keyboard", entity.getName());
        assertEquals(new BigDecimal("89.90"), entity.getPrice());

        assertEquals(productId, mappedBack.id());
        assertEquals("Keyboard", mappedBack.name());
        assertEquals(new BigDecimal("89.90"), mappedBack.price());
    }
}
