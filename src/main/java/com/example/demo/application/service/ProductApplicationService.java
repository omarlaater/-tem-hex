package com.example.demo.application.service;

import com.example.demo.application.port.in.CreateProductCommand;
import com.example.demo.application.port.in.CreateProductUseCase;
import com.example.demo.application.port.in.ListProductsUseCase;
import com.example.demo.application.port.out.ProductRepositoryPort;
import com.example.demo.domain.model.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductApplicationService implements CreateProductUseCase, ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ProductApplicationService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    @Transactional
    public Product createProduct(CreateProductCommand command) {
        Product product = new Product(UUID.randomUUID(), command.name(), command.price());
        return productRepositoryPort.save(product);
    }

    @Override
    public List<Product> listProducts() {
        return productRepositoryPort.findAll();
    }
}
