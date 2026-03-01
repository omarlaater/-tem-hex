package com.example.demo.infrastructure.adapter.out.persistence;

import com.example.demo.application.port.out.ProductRepositoryPort;
import com.example.demo.domain.model.Product;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JpaProductRepositoryAdapter implements ProductRepositoryPort {

    private final SpringDataProductRepository springDataProductRepository;

    public JpaProductRepositoryAdapter(SpringDataProductRepository springDataProductRepository) {
        this.springDataProductRepository = springDataProductRepository;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity(product.id(), product.name(), product.price());
        ProductJpaEntity savedEntity = springDataProductRepository.save(entity);
        return new Product(savedEntity.getId(), savedEntity.getName(), savedEntity.getPrice());
    }

    @Override
    public List<Product> findAll() {
        return springDataProductRepository.findAll()
                .stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getPrice()))
                .toList();
    }
}
