package com.example.demo.application.service;

import com.example.demo.application.port.in.CreateProductCommand;
import com.example.demo.application.port.out.ProductRepositoryPort;
import com.example.demo.domain.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductApplicationServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private ProductApplicationService productApplicationService;

    @Test
    void shouldCreateProduct() {
        when(productRepositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product created = productApplicationService.createProduct(
                new CreateProductCommand("Keyboard", new BigDecimal("79.99"))
        );

        assertNotNull(created.id());
        assertEquals("Keyboard", created.name());
        assertEquals(new BigDecimal("79.99"), created.price());
    }

    @Test
    void shouldListProducts() {
        Product product = new Product(UUID.randomUUID(), "Mouse", new BigDecimal("29.90"));
        when(productRepositoryPort.findAll()).thenReturn(List.of(product));

        List<Product> products = productApplicationService.listProducts();

        assertEquals(1, products.size());
        assertEquals("Mouse", products.get(0).name());
    }
}
