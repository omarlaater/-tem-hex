package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.application.port.in.CreateProductCommand;
import com.example.demo.application.port.in.CreateProductUseCase;
import com.example.demo.application.port.in.ListProductsUseCase;
import com.example.demo.domain.model.Product;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ListProductsUseCase listProductsUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            ListProductsUseCase listProductsUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.listProductsUseCase = listProductsUseCase;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product createdProduct = createProductUseCase.createProduct(
                new CreateProductCommand(request.name(), request.price())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.fromDomain(createdProduct));
    }

    @GetMapping
    public List<ProductResponse> listProducts() {
        return listProductsUseCase.listProducts()
                .stream()
                .map(ProductResponse::fromDomain)
                .toList();
    }
}
