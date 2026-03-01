package com.example.demo.infrastructure.adapter.out.persistence.mapper;

import com.example.demo.domain.model.Product;
import com.example.demo.infrastructure.adapter.out.persistence.ProductJpaEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {

    public ProductJpaEntity toEntity(Product product) {
        return new ProductJpaEntity(product.id(), product.name(), product.price());
    }

    public Product toDomain(ProductJpaEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getPrice());
    }

    public List<Product> toDomainList(List<ProductJpaEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
