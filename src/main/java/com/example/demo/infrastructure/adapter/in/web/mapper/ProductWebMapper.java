package com.example.demo.infrastructure.adapter.in.web.mapper;

import com.example.demo.application.port.in.CreateProductCommand;
import com.example.demo.domain.model.Product;
import com.example.demo.infrastructure.adapter.in.web.CreateProductRequest;
import com.example.demo.infrastructure.adapter.in.web.ProductResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductWebMapper {

    public CreateProductCommand toCreateCommand(CreateProductRequest request) {
        return new CreateProductCommand(request.name(), request.price());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.id(), product.name(), product.price());
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(this::toResponse)
                .toList();
    }
}
